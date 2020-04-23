package com.mostovoy_company.chart;

import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;

public interface LineChartData {

    String getChartName();

    String getTabName();

    void add(int size, LineChartNode node);

    Node getChartContent();

    Node getConfigurationTab();

    void clear();

    void saveConfiguration();

    void collectStatistic(ResponseMessage message, List<Statistic> statistics);

    void parseResponseMessage(ResponseMessage message);

    Map<Integer, List<LineChartNode>> getUnmodifiableChartData();
}