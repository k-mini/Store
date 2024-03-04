package com.kmini.store.dto.response.admin.chart;

import com.kmini.store.dto.response.admin.chart.BarChartDto.BarChartData.BarChartDataSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BarChartDto {

    private BarChartData data;
    private BarChartOption options;

    @NoArgsConstructor
    @Getter @Setter
    public static class BarChartData {
        // 데이터 이름
        private List<String> labels;
        private List<BarChartDataSet> datasets;

        @NoArgsConstructor
        @Getter @Setter
        public static class BarChartDataSet {
            // 데이터셋 제목
            private String label;
            private List<Integer> data = new ArrayList<>();
            private List<String> backgroundColor =
                List.of("rgba(255, 99, 132, 0.2)",
                        "rgba(255, 159, 64, 0.2)",
                        "rgba(255, 205, 86, 0.2)",
                        "rgba(75, 192, 192, 0.2)",
                        "rgba(54, 162, 235, 0.2)",
                        "rgba(153, 102, 255, 0.2)",
                        "rgba(201, 203, 207, 0.2)");
            private List<String> borderColor =
                    List.of(
                    "rgb(255, 99, 132)",
                    "rgb(255, 159, 64)",
                    "rgb(255, 205, 86)",
                    "rgb(75, 192, 192)",
                    "rgb(54, 162, 235)",
                    "rgb(153, 102, 255)",
                    "rgb(201, 203, 207)");
            private int borderWidth = 1;
        }
    }

    @NoArgsConstructor
    @Getter @Setter
    public static class BarChartOption {
    }

    public static BarChartDto toDto(
            List<String> labels, String titleLabel, List<Integer> data, BarChartOption options) {
        BarChartDataSet barChartDataSet = new BarChartDataSet();
        barChartDataSet.setLabel(titleLabel);
        barChartDataSet.setData(data);

        BarChartData barChartData = new BarChartData();
        barChartData.setLabels(labels);
        barChartData.setDatasets(List.of(barChartDataSet));

        return new BarChartDto(barChartData, options);
    }
}
