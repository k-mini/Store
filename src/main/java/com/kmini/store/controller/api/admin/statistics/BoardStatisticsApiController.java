package com.kmini.store.controller.api.admin.statistics;

import com.kmini.store.dto.response.admin.chart.BarChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.service.statistics.BoardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics/boards")
@RequiredArgsConstructor
public class BoardStatisticsApiController {

    private final BoardStatisticsService boardStatisticsService;

    // 월별 게시판 작성 추이
    @GetMapping("/write")
    public ResponseEntity<?> boardsWriteTrends(
            @RequestParam(defaultValue = "2024") Integer year) {

        LineChartDto result = boardStatisticsService.selectBoardWriteStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/category")
    public ResponseEntity<?> boardsCategoryTrends(
            @RequestParam(defaultValue = "2024") Integer year) {

        BarChartDto result = boardStatisticsService.selectBoardCategoryStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }


}
