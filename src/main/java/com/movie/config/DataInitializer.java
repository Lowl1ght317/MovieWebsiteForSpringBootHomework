package com.movie.config;

import com.movie.entity.User;
import com.movie.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 重新加密所有用户的密码
        List<User> users = userMapper.selectList(null);
        String correctHash = passwordEncoder.encode("123456");
        
        for (User user : users) {
            user.setPassword(correctHash);
            userMapper.updateById(user);
        }
        
        System.out.println("已初始化 " + users.size() + " 个用户的密码，统一密码为: 123456");
    }
}
