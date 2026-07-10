package com.movie.controller;

import com.movie.entity.Movie;
import com.movie.service.MovieService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final ChatClient chatClient;
    private final MovieService movieService;

    public AIController(Optional<ChatClient.Builder> chatClientBuilder, MovieService movieService) {
        this.chatClient = chatClientBuilder
                .map(builder -> builder
                        .defaultSystem("你是一个专业的电影推荐助手，精通电影知识，能够根据用户的喜好推荐合适的电影。请用中文回复，语气友好专业。")
                        .build())
                .orElse(null);
        this.movieService = movieService;
    }

    @GetMapping("/chat")
    public Map<String, Object> chat(@RequestParam String msg) {
        Map<String, Object> response = new HashMap<>();
        if (chatClient == null) {
            response.put("success", false);
            response.put("message", "AI服务未配置");
            return response;
        }
        try {
            String result = chatClient.prompt()
                    .user(msg)
                    .call()
                    .content();
            response.put("success", true);
            response.put("content", result);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "AI服务调用失败: " + e.getMessage());
            return response;
        }
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String msg) {
        if (chatClient == null) {
            return Flux.just("AI服务未配置");
        }
        return chatClient.prompt()
                .user(msg)
                .stream()
                .content();
    }

    @GetMapping("/recommend")
    public Map<String, Object> recommend(@RequestParam(required = false) String genre,
                                         @RequestParam(required = false) String actor,
                                         @RequestParam(required = false) String director,
                                         @RequestParam(required = false) String keyword) {
        Map<String, Object> response = new HashMap<>();
        if (chatClient == null) {
            response.put("success", false);
            response.put("message", "AI服务未配置");
            return response;
        }

        try {
            StringBuilder prompt = new StringBuilder("请为我推荐一些电影。");
            if (genre != null && !genre.isEmpty()) {
                prompt.append(" 类型：").append(genre);
            }
            if (actor != null && !actor.isEmpty()) {
                prompt.append(" 演员：").append(actor);
            }
            if (director != null && !director.isEmpty()) {
                prompt.append(" 导演：").append(director);
            }
            if (keyword != null && !keyword.isEmpty()) {
                prompt.append(" 关键词：").append(keyword);
            }
            prompt.append(" 请推荐5部电影，并给出推荐理由。");

            String result = chatClient.prompt()
                    .user(prompt.toString())
                    .call()
                    .content();

            response.put("success", true);
            response.put("content", result);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "AI推荐失败: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/review")
    public Map<String, Object> generateReview(@RequestParam String movieName) {
        Map<String, Object> response = new HashMap<>();
        if (chatClient == null) {
            response.put("success", false);
            response.put("message", "AI服务未配置");
            return response;
        }

        try {
            String prompt = String.format("请为电影《%s》写一篇影评，要求：1. 分析剧情亮点；2. 评价演员表现；3. 给出总体评分和推荐理由。", movieName);

            String result = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            response.put("success", true);
            response.put("content", result);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "影评生成失败: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/search")
    public Map<String, Object> searchMovies(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        if (chatClient == null) {
            response.put("success", false);
            response.put("message", "AI服务未配置");
            return response;
        }

        try {
            String prompt = String.format("根据用户查询「%s」，分析用户可能想找的电影类型或具体电影，并给出相关推荐。", query);

            String result = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            response.put("success", true);
            response.put("content", result);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索分析失败: " + e.getMessage());
            return response;
        }
    }
}
