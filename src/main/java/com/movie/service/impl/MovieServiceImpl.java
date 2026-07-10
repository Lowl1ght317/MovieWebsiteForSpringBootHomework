package com.movie.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Movie;
import com.movie.entity.PlayRecord;
import com.movie.mapper.MovieMapper;
import com.movie.mapper.PlayRecordMapper;
import com.movie.service.MovieService;
import com.movie.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    private final PlayRecordMapper playRecordMapper;
    private final UserService userService;

    public MovieServiceImpl(PlayRecordMapper playRecordMapper, UserService userService) {
        this.playRecordMapper = playRecordMapper;
        this.userService = userService;
    }

    @Override
    public IPage<Movie> getHotMovies(Page<Movie> page) {
        return baseMapper.selectHotMovies(page);
    }

    @Override
    public IPage<Movie> getMoviesByType(Page<Movie> page, String type) {
        return baseMapper.selectByType(page, type);
    }

    @Override
    public IPage<Movie> getMoviesByRegion(Page<Movie> page, String region) {
        return baseMapper.selectByRegion(page, region);
    }

    @Override
    public IPage<Movie> getWeekRank(Page<Movie> page) {
        return baseMapper.selectWeekRank(page);
    }

    @Override
    public IPage<Movie> getMonthRank(Page<Movie> page) {
        return baseMapper.selectMonthRank(page);
    }

    @Override
    public IPage<Movie> getAllRank(Page<Movie> page) {
        return baseMapper.selectAllRank(page);
    }

    @Override
    public IPage<Movie> getRatingRank(Page<Movie> page) {
        return baseMapper.selectByRating(page);
    }

    @Override
    public List<Movie> getMoviesByActor(Long actorId) {
        return baseMapper.selectByActorId(actorId);
    }

    @Override
    public List<Movie> getMoviesByDirector(Long directorId) {
        return baseMapper.selectByDirectorId(directorId);
    }

    @Override
    public List<Movie> searchMovies(String keyword) {
        return baseMapper.searchMovies(keyword);
    }

    @Override
    public boolean canPlay(Long userId, Long movieId) {
        Movie movie = getById(movieId);
        if (movie == null) {
            return false;
        }
        // 如果不是VIP电影，直接可以播放
        if (movie.getIsVip() == null || movie.getIsVip() == 0) {
            return true;
        }
        // VIP电影需要检查用户VIP状态
        return userService.isVip(userId);
    }

    @Override
    @Transactional
    public void recordPlay(Long userId, Long movieId) {
        PlayRecord record = new PlayRecord();
        record.setUserId(userId);
        record.setMovieId(movieId);
        record.setPlayTime(LocalDateTime.now());
        record.setPlayDuration(0);
        record.setCreateTime(LocalDateTime.now());
        playRecordMapper.insert(record);
        playRecordMapper.updatePlayCount(movieId);
    }

    @Override
    public List<Map<String, Object>> getPlayRankData(LocalDate startDate, LocalDate endDate) {
        List<Movie> movies = baseMapper.getPlayRank(startDate, endDate);
        return movies.stream().map(movie -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", movie.getTitle());
            map.put("playCount", movie.getPlayCount());
            map.put("rating", movie.getRating());
            return map;
        }).toList();
    }

    @Override
    public List<Map<String, Object>> getMovieStats() {
        // 直接查询电影表，获取播放量排行数据
        List<Movie> movies = baseMapper.selectList(null);
        return movies.stream().map(movie -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", movie.getTitle());
            map.put("playCount", movie.getPlayCount());
            map.put("rating", movie.getRating());
            map.put("type", movie.getType());
            map.put("region", movie.getRegion());
            map.put("isVip", movie.getIsVip());
            return map;
        }).sorted((a, b) -> {
            Long countA = ((Number) a.get("playCount")).longValue();
            Long countB = ((Number) b.get("playCount")).longValue();
            return Long.compare(countB, countA);
        }).toList();
    }

    @Override
    public List<String> getAllTypes() {
        // 从数据库中获取所有类型，将复合类型（如"科幻/冒险"）拆分为独立类型
        List<String> rawTypes = baseMapper.selectDistinctTypes();
        Set<String> uniqueTypes = new LinkedHashSet<>();
        for (String type : rawTypes) {
            for (String t : type.split("/")) {
                uniqueTypes.add(t.trim());
            }
        }
        return new ArrayList<>(uniqueTypes);
    }

    @Override
    public List<String> getAllRegions() {
        return baseMapper.selectDistinctRegions();
    }
}