package com.kmini.store.domain.admin.statistics.common;

import com.kmini.store.domain.admin.statistics.common.DoughnutChartDto.DoughnutChartData.DoughnutChartDataSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class DoughnutChartDto {

    private DoughnutChartData data;
    private DoughnutChartOption options;

    @NoArgsConstructor
    @Getter @Setter
    public static class DoughnutChartData {
        // 데이터 이름
        private List<String> labels;
        private List<DoughnutChartDataSet> datasets;

        @NoArgsConstructor
        @Getter @Setter
        public static class DoughnutChartDataSet {
            // 데이터셋 제목
            private String label;
            private List<Integer> data = new ArrayList<>();
        }
    }

    @NoArgsConstructor
    @Getter @Setter
    public static class DoughnutChartOption {
    }

    public static DoughnutChartDto toDto(
            List<String> labels, String titleLabel, List<Integer> data, DoughnutChartOption options) {
        DoughnutChartDataSet doughnutChartDataSet = new DoughnutChartDataSet();
        doughnutChartDataSet.setLabel(titleLabel);
        doughnutChartDataSet.setData(data);

        DoughnutChartData doughnutChartData = new DoughnutChartData();
        doughnutChartData.setLabels(labels);
        doughnutChartData.setDatasets(List.of(doughnutChartDataSet));

        return new DoughnutChartDto(doughnutChartData, options);
    }
}
