package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public interface LineChartData {

    String getChartName();

    String getTabName();

    void add(int size, LineChartNode node);

    LineChart<Number, Number> getChart();

    Node getConfigurationTab();

    void clear();

}
