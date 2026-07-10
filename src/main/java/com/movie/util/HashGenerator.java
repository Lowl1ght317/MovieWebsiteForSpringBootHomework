package com.movie.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String hash = encoder.encode(password);
        System.out.println("===HASH_START===");
        System.out.println(hash);
        System.out.println("===HASH_END===");
        System.out.println("验证: " + encoder.matches(password, hash));
    }
}