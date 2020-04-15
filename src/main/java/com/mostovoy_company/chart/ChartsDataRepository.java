package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import javafx.application.Platform;
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