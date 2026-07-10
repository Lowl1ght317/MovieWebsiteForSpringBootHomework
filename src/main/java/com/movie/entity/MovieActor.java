package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("movie_actor")
public class MovieActor {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("movie_id")
    private Long movieId;

    @TableField("actor_id")
    private Long actorId;

    @TableField("role_name")
    private String roleName;

    @TableField("create_time")
    private LocalDateTime createTime;
}