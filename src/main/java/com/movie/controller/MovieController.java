package com.movie.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.movie.entity.Movie;
import com.movie.service.MovieService;
import com.movie.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;
    private final JwtUtil jwtUtil;

    public MovieController(MovieService movieService, JwtUtil jwtUtil) {
        this.movieService = movieService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hot")
    public ResponseEntity<IPage<Movie>> getHotMovies(@RequestParam(defaultValue = "1") int page, 
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getHotMovies(new Page<>(page, size)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<IPage<Movie>> getMoviesByType(@PathVariable String type,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getMoviesByType(new Page<>(page, size), type));
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<IPage<Movie>> getMoviesByRegion(@PathVariable String region,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getMoviesByRegion(new Page<>(page, size), region));
    }

    @GetMapping("/rank/week")
    public ResponseEntity<IPage<Movie>> getWeekRank(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getWeekRank(new Page<>(page, size)));
    }

    @GetMapping("/rank/month")
    public ResponseEntity<IPage<Movie>> getMonthRank(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getMonthRank(new Page<>(page, size)));
    }

    @GetMapping("/rank/all")
    public ResponseEntity<IPage<Movie>> getAllRank(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getAllRank(new Page<>(page, size)));
    }

    @GetMapping("/rank/rating")
    public ResponseEntity<IPage<Movie>> getRatingRank(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getRatingRank(new Page<>(page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String keyword) {
        return ResponseEntity.ok(movieService.searchMovies(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getById(id));
    }

    @PostMapping("/play/{movieId}")
    public ResponseEntity<Map<String, Object>> playMovie(@PathVariable Long movieId,
                                                        @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
            if (!movieService.canPlay(userId, movieId)) {
                response.put("success", false);
                response.put("message", "需要VIP会员才能观看");
                return ResponseEntity.ok(response);
            }
            movieService.recordPlay(userId, movieId);
            Movie movie = movieService.getById(movieId);
            response.put("success", true);
            response.put("videoUrl", movie.getVideoUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllTypes() {
        return ResponseEntity.ok(movieService.getAllTypes());
    }

    @GetMapping("/regions")
    public ResponseEntity<List<String>> getAllRegions() {
        return ResponseEntity.ok(movieService.getAllRegions());
    }
}