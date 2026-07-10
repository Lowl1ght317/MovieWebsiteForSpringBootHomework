package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("movie")
public class Movie {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("original_title")
    private String originalTitle;

    @TableField("type")
    private String type;

    @TableField("region")
    private String region;

    @TableField("release_date")
    private LocalDate releaseDate;

    @TableField("duration")
    private Integer duration;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("rating_count")
    private Integer ratingCount;

    @TableField("play_count")
    private Long playCount;

    @TableField("description")
    private String description;

    @TableField("poster")
    private String poster;

    @TableField("video_url")
    private String videoUrl;

    @TableField("is_vip")
    private Integer isVip;

    @TableField("price")
    private BigDecimal price;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}