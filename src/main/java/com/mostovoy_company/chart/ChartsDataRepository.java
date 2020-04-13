package com.mostovoy_company.chart;

import com.mostovoy_company.kafka.dto.LineChartNode;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ChartsDataRepository {

    private Map<String, LineChartData> charts;

    @Autowired
    public ChartsDataRepository(Map<String, LineChartData> charts) {
        this.charts = charts;
    }

    public void init(AnchorPane objectStationDistance1,
                     AnchorPane objectStationDistance2,
                     AnchorPane clusterCountChartPane,
                     AnchorPane clusterSizeChartPane,
                     AnchorPane redCellsCountLineChart,
                     AnchorPane wayLengthLineChart,
                     AnchorPane ratioDarkRedAndBlackCells) {
        charts.get(ChartNames.CLUSTER_COUNT_CHART).init(clusterCountChartPane, "Зависимость количество кластеров от концентрации");
        charts.get(ChartNames.CLUSTER_SIZE_CHART).init(clusterSizeChartPane, "Средний размер кластеров");
        charts.get(ChartNames.RED_CELLS_ADDED_CHART).init(redCellsCountLineChart, "Количество добавленых красных клеток");
        charts.get(ChartNames.WAY_LENGTHS_CHART).init(wayLengthLineChart, "Средняя длина пути");
        charts.get(ChartNames.RED_CELLS_STATION_DISTANCES_PI_CHART).init(objectStationDistance1, "Расстояние вычисляется с помощью теоремы Пифагора");
        charts.get(ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART).init(objectStationDistance2, "Расстояние вычисляется как количество переходов");
        charts.get(ChartNames.RATIO_DARK_RED_AND_BLACK_CELLS_CHART).init(ratioDarkRedAndBlackCells, "Отношение темнокрасных и черных клеток");
    }

    public void addAll(int size, Map<String, LineChartNode> values) {
        Platform.runLater(() -> values.forEach((name, value) -> add(name, size, value)));
    }

    public void add(String name, int size, LineChartNode node) {
        charts.get(name).add(size, node);
    }

    public void clear() {
        charts.forEach((name, chart) -> chart.clear());
    }
}