# 电影网站管理系统

## 编写目的

本项目是软件学院2024级《Java框架技术》课程的期末作品，旨在运用 Spring Boot + MyBatis Plus 框架，设计与实现一个功能完整的电影网站。通过本项目，学生能够掌握企业级Java Web应用的开发流程，包括需求分析、架构设计、数据库设计、接口开发、权限控制等核心技能，并实践Redis缓存、JWT认证、AI集成等现代技术。

## 技术栈

| 技术 | 版本 | 说明 |
| :--- | :--- | :--- |
| Spring Boot | 3.5.14 | 后端框架 |
| MyBatis Plus | 3.5.7 | ORM框架 |
| Redis | - | 缓存数据库 |
| MySQL | 8.0+ | 关系型数据库 |
| Spring Security | - | 安全框架 |
| JWT | 0.11.5 | 身份认证 |
| Spring AI | 1.1.7 | AI集成（DeepSeek） |
| Thymeleaf | - | 模板引擎 |
| Apache POI | 5.2.5 | Excel导出 |
| Lombok | - | 简化代码 |

## 功能特性

### 1. 用户管理
- **会员注册**：支持新用户注册账号
- **会员登录/退出**：支持用户名密码登录
- **VIP权限**：区分VIP账号和普通账号，VIP可观看VIP专属影片

### 2. 影片展示
- **热播排行**：按播放量展示热门电影
- **分类展示**：按类型（科幻、喜剧、动作等）展示
- **地区展示**：按地区（中国、美国、韩国、日本等）展示

### 3. 电影排行
- **本周排行**：本周播放量排名
- **本月排行**：本月播放量排名
- **全部排行**：历史总播放量排名
- **好评排行**：按评分高低排名

### 4. 主创作品检索
- **演员检索**：根据演员姓名搜索其主演的电影
- **导演检索**：根据导演姓名搜索其执导的电影

### 5. 电影播放
- **播放权限控制**：VIP影片仅限VIP会员观看
- **播放记录**：记录用户观看历史

### 6. 其他功能
- **AI推荐**：基于AI的电影推荐服务
- **数据导出**：支持Excel格式导出数据报表

## 项目结构

```
movie-website/
├── src/main/java/com/movie/
│   ├── controller/     # 控制器层
│   ├── service/        # 服务层
│   │   └── impl/       # 服务实现
│   ├── mapper/         # 数据访问层
│   ├── entity/         # 实体类
│   ├── config/         # 配置类
│   ├── util/           # 工具类
│   └── MovieWebsiteApplication.java
├── src/main/resources/
│   ├── mapper/         # MyBatis映射文件
│   ├── templates/      # Thymeleaf模板
│   ├── application.properties
│   ├── schema.sql      # 数据库初始化脚本
│   └── data.sql        # 测试数据
├── pom.xml
└── README.md
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd movie-website
```

2. **配置数据库**

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE movie_db CHARACTER SET utf8 COLLATE utf8_general_ci;
USE movie_db;
SOURCE src/main/resources/schema.sql;
SOURCE src/main/resources/data.sql;
```

3. **修改配置文件**

编辑 `src/main/resources/application.properties`，配置数据库和Redis连接：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/movie_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password

# Redis配置
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
```

4. **运行项目**

```bash
mvn spring-boot:run
```

或者打包后运行：

```bash
mvn clean package
java -jar target/movie-website-0.0.1-SNAPSHOT.jar
```

### 测试账号

系统预置了以下测试账号，密码均为 `123456`：

| 用户名 | 类型 | 说明 |
| :--- | :--- | :--- |
| vipuser | VIP会员 | 可观看所有影片 |
| vipuser2 | VIP会员 | 可观看所有影片 |
| normaluser | 普通会员 | 只能观看非VIP影片 |
| normaluser2 | 普通会员 | 只能观看非VIP影片 |

## API接口

### 用户接口
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `POST /api/user/logout` - 用户退出

### 电影接口
- `GET /api/movie/hot` - 热播电影列表
- `GET /api/movie/byType` - 按类型查询
- `GET /api/movie/byRegion` - 按地区查询
- `GET /api/movie/rank/week` - 本周排行
- `GET /api/movie/rank/month` - 本月排行
- `GET /api/movie/rank/all` - 全部排行
- `GET /api/movie/rank/rating` - 好评排行
- `GET /api/movie/{id}` - 电影详情
- `POST /api/movie/play` - 播放电影

### 主创接口
- `GET /api/creator/actor/{name}` - 按演员搜索电影
- `GET /api/creator/director/{name}` - 按导演搜索电影

## 数据库设计

### 核心表结构

- **user**：用户表（ID、用户名、密码、邮箱、VIP等级等）
- **movie**：电影表（标题、类型、地区、评分、播放量、VIP标识等）
- **actor**：演员表（姓名、国籍、生日等）
- **director**：导演表（姓名、国籍、生日等）
- **movie_actor**：电影-演员关联表
- **movie_director**：电影-导演关联表
- **play_record**：播放记录表

## 许可证

MIT License

