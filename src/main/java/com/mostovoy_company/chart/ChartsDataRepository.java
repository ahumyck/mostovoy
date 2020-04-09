package com.mostovoy_company.chart;

import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
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
                     AnchorPane wayLengthLineChart) {
        charts.get(ChartNames.CLUSTER_COUNT_CHART).init(clusterCountChartPane, "Зависимость количество кластеров от концентрации");
        charts.get(ChartNames.CLUSTER_SIZE_CHART).init(clusterSizeChartPane, "Средний размер кластеров");
        charts.get(ChartNames.RED_CELLS_ADDED_CHART).init(redCellsCountLineChart, "Количество добавленых красных клеток");
        charts.get(ChartNames.WAY_LENGTHS_CHART).init(wayLengthLineChart, "Средняя длина пути");
        charts.get(ChartNames.RED_CELLS_STATION_DISTANCES_PI_CHART).init(objectStationDistance1, "Расстояние вычисляется с помощью теоремы Пифагора");
        charts.get(ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART).init(objectStationDistance2, "Расстояние вычисляется как количество переходов");
    }

    public void addAll(int size, Map<String, LineChartNode> values) {
        values.forEach((name, value) -> add(name, size, value));
    }

    public void add(String name, int size, LineChartNode node) {
        charts.get(name).add(size, node);
    }

    public void clear() {
        charts.forEach((name, chart) -> chart.clear());
    }
}