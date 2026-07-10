package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("movie_director")
public class MovieDirector {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("movie_id")
    private Long movieId;

    @TableField("director_id")
    private Long directorId;

    @TableField("create_time")
    private LocalDateTime createTime;
}