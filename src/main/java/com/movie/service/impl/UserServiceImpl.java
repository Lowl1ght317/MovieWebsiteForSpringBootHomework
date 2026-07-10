package com.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.User;
import com.movie.mapper.UserMapper;
import com.movie.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        if (baseMapper.selectByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (baseMapper.selectByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVipLevel(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = baseMapper.selectByUsername(username);
        if (user == null) {
            user = baseMapper.selectByEmail(username);
        }
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public boolean isVip(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        boolean result = user.isVip();
        // 调试日志
        System.out.println("=== VIP检查 ===");
        System.out.println("用户ID: " + userId);
        System.out.println("用户名: " + user.getUsername());
        System.out.println("VIP等级: " + user.getVipLevel());
        System.out.println("VIP过期时间: " + user.getVipExpireTime());
        System.out.println("当前时间: " + java.time.LocalDateTime.now());
        System.out.println("是否VIP: " + result);
        System.out.println("===============");
        return result;
    }

    @Override
    public void upgradeToVip(Long userId, int months) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        LocalDateTime expireTime = user.getVipExpireTime();
        if (expireTime == null || expireTime.isBefore(LocalDateTime.now())) {
            expireTime = LocalDateTime.now();
        }
        user.setVipLevel(1);
        user.setVipExpireTime(expireTime.plusMonths(months));
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }
}