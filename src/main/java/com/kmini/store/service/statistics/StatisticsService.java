package com.kmini.store.service.statistics;

import com.kmini.store.domain.type.Gender;
import com.kmini.store.dto.response.admin.chart.BarChartDto;
import com.kmini.store.dto.response.admin.chart.DoughnutChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.repository.statistics.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.Gender.WOMAN;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;
    private final List<String> YEAR_LABELS =
            List.of("1월", "2월", "3월", "4월", "5월", "6월"
                    , "7월", "8월", "9월", "10월", "11월", "12월");
    private final List<String> GENDER_LABELS =
            List.of("남", "여");
    private final List<String> AGE_LABELS =
            List.of("10대", "20대", "30대", "40대", "50대", "60대", "70대");

    // 년도 별 유저 가입자수 조회
    @Transactional
    public LineChartDto selectUserJoinStatisticsAnnual(Integer year) {

        String titleLabel = String.format("%d년", year);
        List<Integer> data = userStatisticsRepository.selectUserJoinStatisticsAnnual(year);
        log.debug("{}년도 연간 유저 가입자수 조회 완료 data = {}",year,data);

        return LineChartDto.toDto(YEAR_LABELS, titleLabel, data, null);
    }

    // 가입자수 성비 조회
    @Transactional
    public DoughnutChartDto selectUserGenderRatioStatisticsAnnual(Integer year) {

        String titleLabel = String.format("%d년", year);
        Map<Gender, Integer> genderJoinMap = userStatisticsRepository.selectUserGenderCntStatisticsAnnual(year);

        List<Long> data = calculateGenderRatio(genderJoinMap);

        log.debug("{}년도 사용자 남/녀 가입자수 조회 완료 genderJoinMap = {}", year, genderJoinMap);
        log.debug("{}년도 사용자 남/녀 비율 계산 완료 data = {}", year, data);

        return DoughnutChartDto.toDto(GENDER_LABELS, titleLabel, data, null);
    }

    public BarChartDto selectUserAgeStatisticsAnnual(Integer year) {

        String titleLabel = String.format("%d년", year);
        List<Integer> data = userStatisticsRepository.selectUserAgeStatisticsAnnual(year);
        System.out.printf("%d년도 사용자 연령대 조회 완료 data = %s %n", year, data);

        return BarChartDto.toDto(AGE_LABELS, titleLabel, data, null);
    }

    private List<Long> calculateGenderRatio(Map<Gender, Integer> genderJoinMap) {

        int manCnt = genderJoinMap.get(MAN);
        int womanCnt = genderJoinMap.get(WOMAN);
        double totalCnt = manCnt + womanCnt;

        if (totalCnt == 0) {
            List.of(0,0);
        }
        long manPercent = Math.round((double) manCnt / totalCnt * 100);
        long womanPercent = 100 - manPercent;

        return List.of(manPercent, womanPercent);
    }
}
