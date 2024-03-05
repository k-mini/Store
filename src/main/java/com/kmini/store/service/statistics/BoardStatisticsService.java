package com.kmini.store.service.statistics;

import com.kmini.store.dto.response.admin.chart.BarChartDto;
import com.kmini.store.dto.response.admin.chart.LineChartDto;
import com.kmini.store.repository.statistics.BoardStatisticsRepository;
import com.kmini.store.repository.statistics.BoardStatisticsRepository.CategoryRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardStatisticsService {

    private final BoardStatisticsRepository boardStatisticsRepository;
    private final List<String> MONTH_LABELS =
            List.of("1월", "2월", "3월", "4월", "5월", "6월"
                    , "7월", "8월", "9월", "10월", "11월", "12월");
    private final List<String> CATEGORY_LABELS =
            List.of("전자 게시판", "의류 게시판", "자유 게시판", "공지사항 게시판");

    // 월별 게시판 작성 추이
    @GetMapping("/write")
    public LineChartDto selectBoardWriteStatisticsAnnual(Integer year) {

        String titleLabel = String.format("%d년", year);
        List<Integer> data = boardStatisticsRepository.selectBoardWriteStatisticsAnnual(year);

        return LineChartDto.toDto(MONTH_LABELS, titleLabel, data,null);
    }

    // 년도별 게시판 카테고리 비율
    @GetMapping("/category")
    public BarChartDto selectBoardCategoryStatisticsAnnual(Integer year) {

        List<String> categoryLabels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<BoardStatisticsRepository.StatisticsCategoryRow> statisticsResult = boardStatisticsRepository.selectBoardCategoryStatisticsAnnual(year);

        statisticsResult
                .forEach((categoryRow) -> {
                    categoryLabels.add(String.format("%s 게시판", categoryRow.getCategoryKoName()));
                    data.add(categoryRow.getCnt());
                });

        return BarChartDto.toDto(categoryLabels, null, data,null);
    }

}
