package com.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.Director;

import java.util.List;

public interface DirectorService extends IService<Director> {
    List<Director> searchByName(String name);
}