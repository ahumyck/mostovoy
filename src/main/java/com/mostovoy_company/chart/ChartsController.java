package com.mostovoy_company.chart;

import com.mostovoy_company.services.MainService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
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
    public AnchorPane statisticChartsAnchorPane;
    @FXML
    public TabPane statisticChartsTabPane;

    private List<LineChartData> charts;
    private ChartsDataRepository chartsDataRepository;
    private MainService mainService;


    public AnchorPane getStatisticCharts() {
        return statisticChartsAnchorPane;
    }

    public ChartsController(List<LineChartData> charts, ChartsDataRepository chartsDataRepository, @Qualifier("defaultService") MainService mainService) {
        this.charts = charts;
        this.chartsDataRepository = chartsDataRepository;
        this.mainService = mainService;
        System.out.println("Charts controller constract");
    }

    @FXML
    public void initialize() {
        System.out.println("Charts controller init");
        charts.forEach(chartData -> {
            Tab tab = new Tab();
            tab.setText(chartData.getTabName());
            tab.setContent(chartData.getChart());
            statisticChartsTabPane.getTabs().add(tab);
        });

        applyExperiment.setOnAction(event -> {
            Map<Integer, Integer> map = new LinkedHashMap<>();
            List<Integer> sizes = Arrays.stream(this.matrixSize.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            List<Integer> counts = countParser(this.matrixCount.getText(), sizes.size());
            chartsDataRepository.clear();
            for (int i = 0; i < sizes.size(); i++) {
                map.put(sizes.get(i), counts.get(i));
            }
            log.info("=> init: " + map);
            map.forEach((size, count) ->
                    DoubleStream.iterate(0.01, x -> x + 0.025)
                            .limit(100)
                            .filter(x -> x > 0)
                            .filter(x -> x <= 0.651)
                            .forEach(probability -> mainService.addExperimentsDescription(count, size, probability)));
            mainService.consume();
        });
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

