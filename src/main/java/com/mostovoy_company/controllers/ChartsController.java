package com.mostovoy_company.controllers;

import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.chart.LightningBoltIndependentChart;
import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.services.ConsumeProperties;
import com.mostovoy_company.services.MainService;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
@FxmlView("charts.fxml")
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
    @FXML
    public Button snapshotButton;
    @FXML
    public Button saveButton;
    @FXML
    public Button uploadButton;

    private final FxWeaver fxWeaver;
    private final List<LightningBoltIndependentChart> lightningBoltIndependentCharts;
    private final ChartsDataRepository chartsDataRepository;
    private final MainService mainService;
    private final List<LightningBoltDependentChart> lightningBoltDependentCharts;


    public ChartsController(FxWeaver fxWeaver,
                            List<LightningBoltIndependentChart> lightningBoltIndependentCharts,
                            List<LightningBoltDependentChart> lightningBoltDependentCharts,
                            ChartsDataRepository chartsDataRepository,
                            @Qualifier("defaultService") MainService mainService) {
        this.fxWeaver = fxWeaver;
        this.lightningBoltIndependentCharts = lightningBoltIndependentCharts;
        this.chartsDataRepository = chartsDataRepository;
        this.mainService = mainService;
        this.lightningBoltDependentCharts = lightningBoltDependentCharts;
    }

    public Node getContent() {
        return statisticChartsAnchorPane;
    }

    @FXML
    public void initialize() {
        VBox.setVgrow(statisticChartsTabPane, Priority.ALWAYS);
        configurationWindow.setVisible(false);
        configurationWindow.setOnMouseClicked(mouseEvent -> fxWeaver.loadController(ChartConfigurationController.class).show(getCurrentAvailableCharts()));
        configurationWindow.setVisible(true);
        applyExperiment.setOnAction(event -> {
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
        snapshotButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.pngExtensionFilter)
                    .ifPresent(file -> {
                        Node node = statisticChartsTabPane.getSelectionModel().getSelectedItem().getContent();
                        WritableImage writableImage = new WritableImage((int) statisticChartsTabPane.getWidth(), (int) statisticChartsTabPane.getHeight());
                        node.snapshot(new SnapshotParameters(), writableImage);
                        try {
                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                        } catch (IOException ex) {
                            log.error(ex.getMessage(), ex);
                        }
                    });
        });
        buildChartsTabPane();
        performLightning.setOnAction(actionEvent -> buildChartsTabPane());

        saveButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter)
                    .ifPresent(file -> chartsDataRepository.saveChartsToJSON(file.getPath()));
        });
        uploadButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getSingleFile(FileChooserController.jsonExtensionFilter)
                    .ifPresent(file -> {
                        chartsDataRepository.clear();
                        chartsDataRepository.restoreChartsFormJSON(file.getPath());
                    });
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