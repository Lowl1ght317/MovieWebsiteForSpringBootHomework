package com.movie.controller;

import com.movie.service.MovieService;
import com.movie.util.PoiUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final MovieService movieService;

    public ReportController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportPlayRank(@RequestParam String startDate, 
                                                 @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            
            List<Map<String, Object>> data = movieService.getPlayRankData(start, end);
            byte[] excelBytes = PoiUtil.generatePlayRankExcel(data);
            
            String filename = "电影播放榜单_" + startDate + "_" + endDate + ".xlsx";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/chart/data")
    public ResponseEntity<List<Map<String, Object>>> getChartData(@RequestParam String startDate,
                                                                 @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return ResponseEntity.ok(movieService.getPlayRankData(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 获取电影统计数据（不依赖播放记录，直接查询电影表）
    @GetMapping("/stats")
    public ResponseEntity<List<Map<String, Object>>> getMovieStats() {
        return ResponseEntity.ok(movieService.getMovieStats());
    }

    // 导出全部电影榜单
    @GetMapping("/export-all")
    public ResponseEntity<byte[]> exportAllMovies() {
        try {
            List<Map<String, Object>> data = movieService.getMovieStats();
            byte[] excelBytes = PoiUtil.generateAllMoviesExcel(data);
            
            String filename = "全部电影榜单_" + LocalDate.now() + ".xlsx";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", new String(filename.getBytes("UTF-8"), "ISO-8859-1"));
            
            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}