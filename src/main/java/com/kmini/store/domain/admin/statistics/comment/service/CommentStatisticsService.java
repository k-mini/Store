package com.kmini.store.domain.admin.statistics.comment.service;

import com.kmini.store.domain.admin.statistics.comment.repository.CommentStatisticsRepository;
import com.kmini.store.global.constants.Gender;
import com.kmini.store.domain.admin.statistics.common.DoughnutChartDto;
import com.kmini.store.domain.admin.statistics.common.LineChartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kmini.store.global.util.CustomStatisticsUtils.calculateGenderRatio;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentStatisticsService {

    private final CommentStatisticsRepository commentStatisticsRepository;
    private final List<String> MONTH_LABELS =
            List.of("1월", "2월", "3월", "4월", "5월", "6월"
                    , "7월", "8월", "9월", "10월", "11월", "12월");
    private final List<String> CATEGORY_LABELS =
            List.of("전자 게시판", "의류 게시판", "자유 게시판", "문의 게시판");
    private final List<String> GENDER_LABELS = List.of("남", "여");

    // 월별 댓글 작성수 조회
    @Transactional
    public LineChartDto selectCommentWriteStatisticsAnnual(Integer year) {

        String titleLabel = String.format("%d년", year);
        List<Integer> data = commentStatisticsRepository.selectCommentWriteStatisticsAnnual(year);
        log.debug("{}년도 월별 댓글 작성 수 조회 완료 data = {}",year, data);

        return LineChartDto.toDto(MONTH_LABELS, titleLabel, data, null);
    }


    @Transactional
    public DoughnutChartDto selectCommentCategoryStatisticsAnnual(Integer year) {

        List<String> categoryLabels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        List<CommentStatisticsRepository.StatisticsCategoryRow> statisticsResult = commentStatisticsRepository.selectCommentCategoryStatisticsAnnual(year);

        statisticsResult
                .forEach((categoryRow) -> {
                    categoryLabels.add(String.format("%s 게시판", categoryRow.getCategoryKoName()));
                    data.add(categoryRow.getCnt());
                });

        return DoughnutChartDto.toDto(categoryLabels, null, data,null);
    }

    public DoughnutChartDto selectCommentGenderStatisticsAnnual(Integer year) {

        Map<Gender, Integer> genderCommentMap = commentStatisticsRepository.selectCommentGenderStatisticsAnnual(year);
        List<Integer> data = calculateGenderRatio(genderCommentMap);

        return DoughnutChartDto.toDto(GENDER_LABELS, null, data, null);
    }

}
