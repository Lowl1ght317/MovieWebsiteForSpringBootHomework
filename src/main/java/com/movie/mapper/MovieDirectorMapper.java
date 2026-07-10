package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieDirector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieDirectorMapper extends BaseMapper<MovieDirector> {
    List<MovieDirector> selectByMovieId(@Param("movieId") Long movieId);
    List<MovieDirector> selectByDirectorId(@Param("directorId") Long directorId);
}