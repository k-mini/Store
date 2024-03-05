package com.kmini.store.controller.api.admin.statistics;


import com.kmini.store.dto.response.admin.chart.BarChartDto;
import com.kmini.store.dto.response.admin.chart.DoughnutChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.service.statistics.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics/users")
@RequiredArgsConstructor
public class UserStatisticsApiController {

    private final UserStatisticsService userStatisticsService;

    // 년도별 가입자수 통계
    @GetMapping("/join")
    public ResponseEntity<?> usersJoinTrends(
            @RequestParam(defaultValue = "2024") Integer year) {

        LineChartDto result = userStatisticsService.selectUserJoinStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

    // 이용자 성비 통계
    @GetMapping("/gender")
    public ResponseEntity<?> usersGenderRatio(
            @RequestParam(defaultValue = "2024") Integer year){

        DoughnutChartDto result = userStatisticsService.selectUserGenderRatioStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }
    
    // 이용자 연령대 통계
    @GetMapping("/age")
    public ResponseEntity<?> usersAgeRatio(
            @RequestParam(defaultValue = "2024") Integer year) {

        BarChartDto result = userStatisticsService.selectUserAgeStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }
}
