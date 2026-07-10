DROP DATABASE movie_db;
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `movie_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `movie_db`;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    vip_level INT DEFAULT 0,
    vip_expire_time DATETIME,
    avatar VARCHAR(255),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    original_title VARCHAR(200),
    type VARCHAR(100),
    region VARCHAR(100),
    release_date DATE,
    duration INT,
    rating DECIMAL(3,1),
    rating_count INT DEFAULT 0,
    play_count BIGINT DEFAULT 0,
    description TEXT,
    poster VARCHAR(255),
    video_url VARCHAR(500),
    is_vip INT DEFAULT 0,
    price DECIMAL(10,2),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    english_name VARCHAR(100),
    birthday DATE,
    nationality VARCHAR(50),
    avatar VARCHAR(255),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS director (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    english_name VARCHAR(100),
    birthday DATE,
    nationality VARCHAR(50),
    avatar VARCHAR(255),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS movie_actor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,
    role_name VARCHAR(100),
    create_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id),
    FOREIGN KEY (actor_id) REFERENCES actor(id)
);

CREATE TABLE IF NOT EXISTS movie_director (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    director_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id),
    FOREIGN KEY (director_id) REFERENCES director(id)
);

CREATE TABLE IF NOT EXISTS play_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    play_time DATETIME NOT NULL,
    play_duration INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

