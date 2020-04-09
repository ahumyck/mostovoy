package com.mostovoy_company.chart;

import com.mostovoy_company.kafka.dto.LineChartNode;
import javafx.scene.layout.AnchorPane;

public interface LineChartData {

    void add(int size, LineChartNode node);

    void init(AnchorPane anchorPane, String title);

    void clear();

}
