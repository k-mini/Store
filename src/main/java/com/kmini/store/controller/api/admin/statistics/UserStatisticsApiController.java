package com.kmini.store.controller.api.admin.statistics;


import com.kmini.store.dto.response.admin.chart.BarChartDto;
import com.kmini.store.dto.response.admin.chart.DoughnutChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.service.statistics.StatisticsService;
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

    private final StatisticsService statisticsService;

    // 년도별 가입자수 통계
    @GetMapping("/join")
    public ResponseEntity<?> usersJoin(
            @RequestParam(defaultValue = "2024") Integer year) {

        LineChartDto result = statisticsService.selectUserJoinStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }

    // 이용자 성비 통계
    @GetMapping("/ratio")
    public ResponseEntity<?> usersRatio(
            @RequestParam(defaultValue = "2024") Integer year){

        DoughnutChartDto result = statisticsService.selectUserGenderRatioStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }
    
    // 이용자 연령대 통계
    @GetMapping("/age")
    public ResponseEntity<?> usersAge(
            @RequestParam(defaultValue = "2024") Integer year) {

        BarChartDto result = statisticsService.selectUserAgeStatisticsAnnual(year);

        return ResponseEntity.ok(result);
    }
}
