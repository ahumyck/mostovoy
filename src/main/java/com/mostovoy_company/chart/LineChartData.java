package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.AnchorPane;

public interface LineChartData {

    String getChartName();

    String getTabName();

    void add(int size, LineChartNode node);

    LineChart<Number, Number> getChart();

    void clear();

}
