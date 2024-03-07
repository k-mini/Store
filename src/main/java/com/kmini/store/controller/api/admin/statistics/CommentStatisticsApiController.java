package com.kmini.store.controller.api.admin.statistics;

import com.kmini.store.dto.response.admin.chart.DoughnutChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.service.statistics.CommentStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics/comments")
@RequiredArgsConstructor
public class CommentStatisticsApiController {

    private final CommentStatisticsService commentStatisticsService;

    // 월별 댓글 작성 추이
    @GetMapping("/write")
    public ResponseEntity<?> commentsWriteTrends(
            @RequestParam(defaultValue = "2024") Integer year) {

        LineChartDto result = commentStatisticsService.selectCommentWriteStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

    // 년도 별 작성한 댓글들이 어느 카테고리에 속하는지 비율
    @GetMapping("/category")
    public ResponseEntity<?> commentsCategoryRatio(
            @RequestParam(defaultValue = "2024") Integer year) {

        DoughnutChartDto result = commentStatisticsService.selectCommentCategoryStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

    // 년도 별 작성한 댓글의 남/녀 비율
    @GetMapping("/gender")
    public ResponseEntity<?> commentsGenderRatio(
            @RequestParam(defaultValue = "2024") Integer year) {

        DoughnutChartDto result = commentStatisticsService.selectCommentGenderStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

}
