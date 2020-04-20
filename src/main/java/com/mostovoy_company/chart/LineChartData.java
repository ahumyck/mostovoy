package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public interface LineChartData {

    String getChartName();

    String getTabName();

    void add(int size, LineChartNode node);

    Node getChartContent();

    Node getConfigurationTab();

    void clear();

    void saveConfiguration();
}