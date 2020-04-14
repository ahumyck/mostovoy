package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseLineChartData implements LineChartData{

    private Painter painter;

    private LineChart<Number, Number> lineChart;

    private Map<Integer, ObservableList<XYChart.Data>> chartData;

    public BaseLineChartData(Painter painter) {
        this.painter = painter;
        this.chartData = new ConcurrentHashMap<>();
    }

    @Override
    public void init(AnchorPane anchorPane, String title) {
        this.lineChart = painter.paintEmptyLineChart(anchorPane, title);
    }

    public void add(int size, LineChartNode node){
        if(chartData.get(size) == null) {
            ObservableList<XYChart.Data> series = FXCollections.observableArrayList();
            painter.addObservableSeries(lineChart, "Mat size " + size, series);
            chartData.put(size, series);
        }
        chartData.get(size).add(convertToXYChartData(node));
    }

    private XYChart.Data convertToXYChartData(LineChartNode node) {
        XYChart.Data dot = new XYChart.Data(node.x, node.y);
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        dot.setNode(rect);
        return dot;
    }

    public void clear(){
        chartData.clear();
        lineChart.getData().clear();
    }
}
