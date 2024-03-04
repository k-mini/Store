package com.kmini.store.dto.response.admin.chart;

import com.kmini.store.dto.response.admin.chart.LineChartDto.LineChartData.LineChartDataSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LineChartDto {

    private LineChartData data;
    private LineChartOption options;

    @NoArgsConstructor
    @Getter @Setter
    public static class LineChartData {
        // 데이터 이름
        private List<String> labels;
        private List<LineChartDataSet> datasets;

        @NoArgsConstructor
        @Getter @Setter
        public static class LineChartDataSet {
            // 데이터셋 제목
            private String label;
            private double lineTension = 0.3;
            private int pointRadius = 3;
            private int pointHoverRadius = 15;
            private int pointHitRadius = 15;
            private int pointBorderWidth = 2;
            private List<Integer> data = new ArrayList<>();
        }
    }

    @NoArgsConstructor
    @Getter @Setter
    public static class LineChartOption {
    }

    public static LineChartDto toDto(
            List<String> labels, String titleLabel, List<Integer> data, LineChartOption options) {
        LineChartDataSet lineChartDataSet = new LineChartDataSet();
        lineChartDataSet.setData(data);
        lineChartDataSet.setLabel(titleLabel);

        LineChartData lineChartData = new LineChartData();
        lineChartData.setLabels(labels);
        lineChartData.setDatasets(List.of(lineChartDataSet));

        return new LineChartDto(lineChartData, options);
    }
}
