package com.movie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MovieService extends IService<Movie> {
    IPage<Movie> getHotMovies(Page<Movie> page);
    IPage<Movie> getMoviesByType(Page<Movie> page, String type);
    IPage<Movie> getMoviesByRegion(Page<Movie> page, String region);
    IPage<Movie> getWeekRank(Page<Movie> page);
    IPage<Movie> getMonthRank(Page<Movie> page);
    IPage<Movie> getAllRank(Page<Movie> page);
    IPage<Movie> getRatingRank(Page<Movie> page);
    List<Movie> getMoviesByActor(Long actorId);
    List<Movie> getMoviesByDirector(Long directorId);
    List<Movie> searchMovies(String keyword);
    boolean canPlay(Long userId, Long movieId);
    void recordPlay(Long userId, Long movieId);
    List<Map<String, Object>> getPlayRankData(LocalDate startDate, LocalDate endDate);
    
    // 获取电影统计数据（用于图表展示，不依赖播放记录表）
    List<Map<String, Object>> getMovieStats();
    
    // 获取所有电影类型和地区
    List<String> getAllTypes();
    List<String> getAllRegions();
}