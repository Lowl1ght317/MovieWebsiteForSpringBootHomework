package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieActor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieActorMapper extends BaseMapper<MovieActor> {
    List<MovieActor> selectByMovieId(@Param("movieId") Long movieId);
    List<MovieActor> selectByActorId(@Param("actorId") Long actorId);
}