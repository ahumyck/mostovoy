package com.mostovoy_company;

import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.filling.customs.*;
import com.mostovoy_company.paint.Painter;
import com.mostovoy_company.services.MainService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.DISCRETE;
import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.PYTHAGORAS;

//import com.mostovoy_company.stat.NormalizedStatManager;

@Component
@FxmlView("sample.fxml")
@Slf4j
public class Controller {

    private ChartsDataRepository chartsDataRepository;

    private MainService mainService;
    private ExperimentManager experimentManager;
    private final Painter painter = new Painter();

    @FXML
    public Label currentClustersCount;

    @FXML
    public AnchorPane objectStationDistance1;

    @FXML
    public AnchorPane objectStationDistance2;

    @FXML
    public AnchorPane ratioDarkRedAndBlackCells;

    @FXML
    public Button distanceCalculatorType;

    @FXML
    public Label redCellsLabel;

    @FXML
    public Label shortestPathLabel;

    @FXML
    private ListView<Experiment> experimentListView;

    @FXML
    private AnchorPane gridPane;

    @FXML
    public Label probabilityLabel;

    @FXML
    public TextField experimentNumber;

    @FXML
    public ComboBox<GridSize> gridSize;

    @FXML
    public TextField fillingProbability;

    @FXML
    public Label gridSizeLabel;
    @FXML
    public ComboBox<FillingType> fillingTypes;

    @FXML
    public Button applyConfiguration;

    @FXML
    public Button applyExperiment;

    @FXML
    public TextField matrixSize;

    @FXML
    public TextField matrixCount;

    @FXML
    public AnchorPane clusterCountChartPane;

    @FXML
    public AnchorPane clusterSizeChartPane;

    @FXML
    public Tab gridTab;

    @FXML
    public Tab lightningBoltTab;

    @FXML
    public AnchorPane lightningBoltPane;

    @FXML
    public AnchorPane redCellsCountLineChart;

    @FXML
    public AnchorPane wayLengthLineChart;

//    @FXML
//    public CheckBox tapeCheckBox;

    @FXML
    public TextField tapeCount;

    public Controller(ChartsDataRepository chartsDataRepository, @Qualifier("defaultService") MainService mainService,
                      ExperimentManager experimentManager) {
        this.chartsDataRepository = chartsDataRepository;
        this.mainService = mainService;
        this.experimentManager = experimentManager;
    }

    @FXML
    public void initialize() {
        chartsDataRepository.init(objectStationDistance1,
                objectStationDistance2,
                clusterCountChartPane,
                clusterSizeChartPane,
                redCellsCountLineChart,
                wayLengthLineChart,
                ratioDarkRedAndBlackCells
        );
//        tapeCheckBox.setSelected(false);
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(),
                new MaltTestFillingType(),
                new HorizontalLineFillingType(),
                new SquareFillingType(),
                new TriangleFillingType(),
                new VerticalLineFillingType(),
                new ChessFillingType()));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
            currentClustersCount.setText("Количество кластеров: " + experiment.getMatrix().getClusterCounter());
            redCellsLabel.setText("Красных клеток: " + experiment.getStatistic().getRedCellCount());
            shortestPathLabel.setText("Расстояние: " + experiment.getDistance());
        });
//        tapeCheckBox.setOnAction(event -> {
//            paintByDistanceResolverAndCheckBox();
//        });
        distanceCalculatorType.setOnAction(actionEvent -> {
            paintByDistanceResolverAndCheckBox();
        });
        fillingProbability.setVisible(false);
        probabilityLabel.setVisible(false);
        gridSize.setVisible(false);
        gridSizeLabel.setVisible(false);
        fillingTypes.setOnAction(event -> {
            if (fillingTypes.getValue() instanceof RandomFillingType) {
                fillingProbability.setVisible(true);
                probabilityLabel.setVisible(true);
                gridSize.setVisible(true);
                gridSizeLabel.setVisible(true);
            } else if (fillingTypes.getValue() instanceof CustomTestFillingType) {
                gridSize.setVisible(false);
                gridSizeLabel.setVisible(false);
                fillingProbability.setVisible(false);
                probabilityLabel.setVisible(false);
            }
        });
        applyConfiguration.setOnAction(actionEvent -> {
            String txt = experimentNumber.getText();
            int number = Integer.parseInt(txt);
            FillingType fillingType = fillingTypes.getValue();
            if (fillingType instanceof RandomFillingType) {
                int size = gridSize.getValue().getValue();
                double probability = Double.parseDouble(fillingProbability.getText());
                ((RandomFillingType) fillingType).setPercolationProbability(probability);
                ((RandomFillingType) fillingType).setSize(size);
                experimentListView.setItems(experimentManager.initializeExperimentsParallel(number, fillingType));
            } else if (fillingType instanceof CustomTestFillingType) {
                experimentListView.setItems(experimentManager.initializeExperimentsParallel(number, fillingType));
            }
        });

        applyExperiment.setOnAction(event -> {
            Map<Integer, Integer> map = new HashMap<>();
            List<Integer> sizes = Arrays.stream(this.matrixSize.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            List<Integer> counts = Arrays.stream(this.matrixCount.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            chartsDataRepository.clear();
            for (int i = 0; i < sizes.size(); i++) {
                map.put(sizes.get(i), counts.get(i));
            }
            log.info("=> init: " + map);
            map.forEach((size, count) ->
                    DoubleStream.iterate(0.01, x -> x + 0.01)
                            .limit(100)
                            .filter(x -> x <= 1)
                            .forEach(probability -> mainService.addExperimentsDescription(count, size, probability)));
            mainService.consume();
        });
    }

    void paintByDistanceResolverAndCheckBox() {
        if (distanceCalculatorType.getText().equals(PYTHAGORAS)) {
            distanceCalculatorType.setText(DISCRETE);
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, DISCRETE);
        } else if (distanceCalculatorType.getText().equals(DISCRETE)) {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            distanceCalculatorType.setText(PYTHAGORAS);
            paintByCheckBox(experiment, PYTHAGORAS);
        }
    }

    void paintByCheckBox(Experiment experiment, String type) {
//        int tape = parseInt(tapeCount.getText());
        painter.paintCanvas(gridPane, experiment.getMatrix());
//        if (tapeCheckBox.isSelected()) {
//            painter.paintLightningBoltAndTape(lightningBoltPane, experiment.getPath(), experiment.generateTape(tape),experiment.getProgrammings(type), experiment.getMatrix());
//        } else {
        painter.paintLightningBoltAndRelations(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(type), experiment.getMatrix());
//        }
    }

    int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}