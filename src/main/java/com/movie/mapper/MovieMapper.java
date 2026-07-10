package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.movie.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    IPage<Movie> selectHotMovies(Page<Movie> page);
    IPage<Movie> selectByType(Page<Movie> page, @Param("type") String type);
    IPage<Movie> selectByRegion(Page<Movie> page, @Param("region") String region);
    IPage<Movie> selectWeekRank(Page<Movie> page);
    IPage<Movie> selectMonthRank(Page<Movie> page);
    IPage<Movie> selectAllRank(Page<Movie> page);
    IPage<Movie> selectByRating(Page<Movie> page);
    List<Movie> selectByActorId(@Param("actorId") Long actorId);
    List<Movie> selectByDirectorId(@Param("directorId") Long directorId);
    List<Movie> searchMovies(@Param("keyword") String keyword);
    List<Movie> getPlayRank(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    List<String> selectDistinctTypes();
    List<String> selectDistinctRegions();
}