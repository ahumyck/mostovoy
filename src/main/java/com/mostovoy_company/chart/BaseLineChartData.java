package com.mostovoy_company.chart;

import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseLineChartData implements LineChartData {

    private Painter painter;

    private LineChart<Number, Number> lineChart;

    private boolean isNormalized;

    private double koef = 1.0;

    private Map<Integer, ObservableList<XYChart.Data<Number, Number>>> chartData;

    public BaseLineChartData(Painter painter) {
        this.painter = painter;
        this.chartData = new ConcurrentHashMap<>();
    }

    @Override
    public void init(AnchorPane anchorPane, String title) {
        this.lineChart = painter.paintEmptyLineChart(anchorPane, title);
        lineChart.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {

                    chartData.forEach((size, data) -> {
                        final double k;
                        if (isNormalized) k = 1.0 / getNormalizedKoef(size);
                        else k = getNormalizedKoef(size);
                        data.forEach(d -> d.setYValue((Double) d.getYValue() * k));
                    });
                    isNormalized = !isNormalized;
                }
            }
        });
    }

    public void add(int size, LineChartNode node) {
        if (chartData.get(size) == null) {
            ObservableList<XYChart.Data<Number, Number>> series = FXCollections.observableArrayList();
            painter.addObservableSeries(lineChart, "Mat size " + size, series);
            chartData.put(size, series);
        }
        if (isNormalized) node.y *= getNormalizedKoef(size);
        chartData.get(size).add(convertToXYChartData(node));
    }

    private XYChart.Data<Number, Number> convertToXYChartData(LineChartNode node) {
        XYChart.Data<Number, Number> dot = new XYChart.Data<>(node.x, node.y);
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        dot.setNode(rect);
        return dot;
    }

    public void clear() {
        chartData.clear();
        lineChart.getData().clear();
        isNormalized = false;
    }

    protected abstract double getNormalizedKoef(int size);

}
