package com.movie.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PoiUtil {

    public static byte[] generatePlayRankExcel(List<Map<String, Object>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("电影播放榜单");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"排名", "电影名称", "播放量", "评分"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(style);
            }

            int rowNum = 1;
            int rank = 1;
            for (Map<String, Object> item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rank++);
                row.createCell(1).setCellValue(String.valueOf(item.get("title")));
                row.createCell(2).setCellValue(item.get("playCount") != null ? 
                        ((Number) item.get("playCount")).longValue() : 0);
                row.createCell(3).setCellValue(item.get("rating") != null ? 
                        ((Number) item.get("rating")).doubleValue() : 0);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public static byte[] generateAllMoviesExcel(List<Map<String, Object>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("全部电影榜单");

            // 创建标题样式
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // 添加标题行
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("电影播放榜单 - 全部影片");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 7));

            // 添加表头
            Row headerRow = sheet.createRow(1);
            String[] headers = {"排名", "电影名称", "类型", "地区", "播放量", "评分", "VIP", "视频链接"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 添加数据
            int rowNum = 2;
            int rank = 1;
            for (Map<String, Object> item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rank++);
                row.createCell(1).setCellValue(String.valueOf(item.get("title")));
                row.createCell(2).setCellValue(String.valueOf(item.get("type")));
                row.createCell(3).setCellValue(String.valueOf(item.get("region")));
                row.createCell(4).setCellValue(item.get("playCount") != null ? 
                        ((Number) item.get("playCount")).longValue() : 0);
                row.createCell(5).setCellValue(item.get("rating") != null ? 
                        ((Number) item.get("rating")).doubleValue() : 0);
                
                Integer isVip = item.get("isVip") != null ? ((Number) item.get("isVip")).intValue() : 0;
                row.createCell(6).setCellValue(isVip == 1 ? "VIP" : "免费");
                row.createCell(7).setCellValue(String.valueOf(item.get("videoUrl")));
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}