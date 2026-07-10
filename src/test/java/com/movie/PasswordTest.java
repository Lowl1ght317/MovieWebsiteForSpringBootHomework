package com.movie;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 数据库中的哈希值
        String dbHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq";
        
        // 测试密码
        String password = "123456";
        
        // 验证
        boolean matches = encoder.matches(password, dbHash);
        System.out.println("密码是否匹配: " + matches);
        
        // 生成正确的哈希值
        String correctHash = encoder.encode(password);
        System.out.println("正确的哈希值: " + correctHash);
        System.out.println("数据库哈希长度: " + dbHash.length());
        System.out.println("正确哈希长度: " + correctHash.length());
    }
}