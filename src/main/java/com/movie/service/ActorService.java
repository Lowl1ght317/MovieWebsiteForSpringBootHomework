package com.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.Actor;

import java.util.List;

public interface ActorService extends IService<Actor> {
    List<Actor> searchByName(String name);
}