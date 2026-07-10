package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.PlayRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlayRecordMapper extends BaseMapper<PlayRecord> {
    List<PlayRecord> selectByUserId(@Param("userId") Long userId);
    List<PlayRecord> selectByMovieId(@Param("movieId") Long movieId);
    void updatePlayCount(@Param("movieId") Long movieId);
    List<PlayRecord> selectPlayRank(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}