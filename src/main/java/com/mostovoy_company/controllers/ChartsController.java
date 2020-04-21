package com.mostovoy_company.controllers;

import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.chart.LightningBoltIndependentChart;
import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.services.ConsumeProperties;
import com.mostovoy_company.services.MainService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@FxmlView("charts.fxml")
@Component
@Slf4j
public class ChartsController {
    @FXML
    public Button applyExperiment;
    @FXML
    public TextField matrixSize;
    @FXML
    public TextField matrixCount;
    @FXML
    public VBox statisticChartsAnchorPane;
    @FXML
    public TabPane statisticChartsTabPane;
    @FXML
    public Button configurationWindow;
    @FXML
    public TextField stepProbability;
    @FXML
    public CheckBox performLightning;

    private FxWeaver fxWeaver;
    private List<LightningBoltIndependentChart> lightningBoltIndependentCharts;
    private ChartsDataRepository chartsDataRepository;
    private MainService mainService;
    private List<LightningBoltDependentChart> lightningBoltDependentCharts;


    public Node getContent() {
        return statisticChartsAnchorPane;
    }

    public ChartsController(FxWeaver fxWeaver,
                            List<LightningBoltIndependentChart> lightningBoltIndependentCharts,
                            List<LightningBoltDependentChart> lightningBoltDependentCharts,
                            ChartsDataRepository chartsDataRepository,
                            @Qualifier("kafkaSupportService") MainService mainService) {
        this.fxWeaver = fxWeaver;
        this.lightningBoltIndependentCharts = lightningBoltIndependentCharts;
        this.chartsDataRepository = chartsDataRepository;
        this.mainService = mainService;
        this.lightningBoltDependentCharts = lightningBoltDependentCharts;
    }

    @FXML
    public void initialize() {
        VBox.setVgrow(statisticChartsTabPane, Priority.ALWAYS);
        configurationWindow.setVisible(false);
        configurationWindow.setOnMouseClicked(mouseEvent -> fxWeaver.loadController(ChartConfigurationController.class).show(getCurrentAvailableCharts()));
        applyExperiment.setOnAction(event -> {
            configurationWindow.setVisible(true);
            final double step = Double.parseDouble(stepProbability.getText());
            Map<Integer, Integer> map = new LinkedHashMap<>();
            List<Integer> sizes = Arrays.stream(this.matrixSize.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            List<Integer> counts = countParser(this.matrixCount.getText(), sizes.size());
            ConsumeProperties consumeProperties = new ConsumeProperties();
            consumeProperties.setLightningBoltEnable(performLightning.isSelected());
            buildChartsTabPane();
            chartsDataRepository.clear();
            for (int i = 0; i < sizes.size(); i++) {
                map.put(sizes.get(i), counts.get(i));
            }
            log.info("=> init: " + map);
            mainService.initNewSession();
            map.forEach((size, count) ->
                    DoubleStream.iterate(0.00, x -> x + step)
                            .limit(120)
                            .filter(x -> x >= 0)
                            .filter(x -> x <= 1.01)
                            .forEach(probability -> mainService.addExperimentsDescription(count, size, probability)));
            mainService.consume(consumeProperties);
        });
    }

    private List<LineChartData> getCurrentAvailableCharts() {
        List<LineChartData> charts = new ArrayList<>(lightningBoltIndependentCharts);
        if (performLightning.isSelected()) {
            charts.addAll(lightningBoltDependentCharts);
        }
        return charts;
    }

    private void buildChartsTabPane() {
        statisticChartsTabPane.getTabs().clear();
        getCurrentAvailableCharts().forEach(chart -> statisticChartsTabPane.getTabs().add(createTabForChartData(chart)));
    }

    private Tab createTabForChartData(LineChartData chartData) {
        Tab tab = new Tab();
        tab.setText(chartData.getTabName());
        tab.setContent(chartData.getChartContent());
        return tab;
    }

    private List<Integer> countParser(String text, int howMany) {
        if (text.contains(",")) {
            String replace = text.replace(" ", "");
            return Arrays.stream(replace.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } else {
            List<Integer> count = new ArrayList<>();
            Integer integer = Integer.valueOf(text);
            for (int i = 0; i < howMany; i++) {
                count.add(integer);
            }
            return count;
        }
    }
}