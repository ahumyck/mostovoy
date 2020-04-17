package com.mostovoy_company.chart;

import com.mostovoy_company.ChartConfigurationTab;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.sun.javafx.charts.Legend;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import lombok.var;
import net.rgielen.fxweaver.core.FxWeaver;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class BaseLineChartData implements LineChartData {

    private LineChart<Number, Number> lineChart;

    private ChartConfigurationTab chartConfigurationTab;

    private FxWeaver fxWeaver;

    private boolean isNormalized;

    private Map<Integer, ObservableList<XYChart.Data<Number, Number>>> representationChartData;

    private Map<Integer, List<LineChartNode>> fullChartData;

    public BaseLineChartData(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
        this.representationChartData = new ConcurrentHashMap<>();
        this.fullChartData = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void init() {
        this.lineChart = createLineChart();
        this.chartConfigurationTab = fxWeaver.loadController(ChartConfigurationTab.class);
        lineChart.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    representationChartData.forEach((size, data) -> {
                        final double k;
                        if (isNormalized) k = 1.0 / getNormalizedCoefficient(size);
                        else k = getNormalizedCoefficient(size);
                        data.forEach(d -> d.setYValue((Double) d.getYValue() * k));
                    });
                    isNormalized = !isNormalized;
                }
            }
        });
    }

    synchronized public void add(int size, LineChartNode node) {
        addToFullChartData(size, node);
        addToRepresentationChartData(size, node);
    }

    private void addToFullChartData(int size, LineChartNode node) {
        fullChartData.computeIfAbsent(size, k -> new ArrayList<>());
        fullChartData.get(size).add(node);
    }

    private void addToRepresentationChartData(int size, LineChartNode node) {
        if (checkStartAndFinalProbability(node)) {
            var newNode = convertToXYChartData(node);
            if (isNormalized) newNode.setYValue((Double) newNode.getYValue() * getNormalizedCoefficient(size));
            representationChartData.computeIfAbsent(size, k -> {
                ObservableList<XYChart.Data<Number, Number>> series = FXCollections.observableArrayList();
                addObservableSeries(lineChart, "Mat size " + size, series);
                return series;
            });
            representationChartData.get(size).add(newNode);
        }
    }

    private boolean checkStartAndFinalProbability(LineChartNode node) {
        return node.x >= chartConfigurationTab.getStartProbability() && node.x <= chartConfigurationTab.getFinalProbability();
    }

    private XYChart.Data<Number, Number> convertToXYChartData(LineChartNode node) {
        XYChart.Data<Number, Number> dot = new XYChart.Data<>(node.x, node.y);
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        dot.setNode(rect);
        return dot;
    }

    public Node getConfigurationTab() {
        return this.chartConfigurationTab.getContent();
    }

    public void clear() {
        representationChartData.clear();
        lineChart.getData().clear();
        fullChartData.clear();
        isNormalized = false;
    }

    protected abstract double getNormalizedCoefficient(int size);

    private LineChart<Number, Number> createLineChart() {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setTitle(getChartName());
        return numberLineChart;
    }

    public LineChart<Number, Number> getChart() {
        return this.lineChart;
    }

    public void addObservableSeries(LineChart<Number, Number> chart, String title, ObservableList<XYChart.Data<Number, Number>> data) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(title);
        series.setData(data);
        chart.getData().add(series);
        chart.getChildrenUnmodifiable().stream()
                .filter(child -> child instanceof Legend)
                .flatMap(legend -> ((Legend) legend).getItems().stream())
                .forEach(item -> {
                            XYChart.Series<Number, Number> s = chart.getData().stream().filter(d -> d.getName().equals(item.getText())).collect(Collectors.toList()).get(0);
                            item.getSymbol().setCursor(Cursor.HAND);
                            item.getSymbol().setOnMouseClicked(event1 -> s.getNode().setVisible(!s.getNode().isVisible()));
                        }
                );

    }

    public void saveConfiguration() {
        this.chartConfigurationTab.saveConfiguration();
        if (this.chartConfigurationTab.isChanged())
            reInitRepresentationChartData();
    }

    private void reInitRepresentationChartData() {
        this.representationChartData.clear();
        this.lineChart.getData().clear();
        fullChartData.forEach((size, data) -> {
            data.forEach(node -> {
                addToRepresentationChartData(size, node);
            });
        });
    }
}
