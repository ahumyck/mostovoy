package com.mostovoy_company;

import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.filling.customs.*;
import com.mostovoy_company.paint.Painter;
import com.mostovoy_company.stat.NormalizedStatManager;
import com.mostovoy_company.stat.StatManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.DISCRETE;
import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.PYTHAGORAS;

@Component
@FxmlView("sample.fxml")
public class Controller {

    private DefaultService defaultService;
    private ExperimentManager experimentManager = new ExperimentManager();
    private final Painter painter = new Painter();
    private StatManager statManager = new NormalizedStatManager();


    @FXML
    public Label currentClustersCount;

    @FXML
    public AnchorPane objectStationDistance1;

    @FXML
    public AnchorPane objectStationDistance2;

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

//    @FXML
//    public TextField startProbability;

//    @FXML
//    public TextField endProbability;

//    @FXML
//    public TextField stepProbability;

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

    @FXML
    public CheckBox tapeCheckBox;

    @FXML
    public TextField tapeCount;

//    public Controller(MainService mainService, NeKafkaService neKafkaService) {
//        this.mainService = mainService;
//        this.neKafkaService = neKafkaService;
//    }


    public Controller(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    @FXML
    public void initialize() {
        tapeCheckBox.setSelected(false);
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(),
                new MaltTestFillingType(),
                new HorizontalLineFillingType(),
                new SquareFillingType(),
                new TriangleFillingType(),
                new VerticalLineFillingType()));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
            currentClustersCount.setText("Количество кластеров: " + experiment.getMatrix().getClusterCounter());
            redCellsLabel.setText("Красных клеток: " + experiment.getRedCellsCounter());
            shortestPathLabel.setText("Расстояние: " + experiment.getDistance());
        });
        tapeCheckBox.setOnAction(event -> {
            paintByDistanceResolverAndCheckBox();
        });
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
//            double startProbability = Double.parseDouble(this.startProbability.getText());
//            double endProbability = Double.parseDouble(this.endProbability.getText());
//            double stepProbability = Double.parseDouble(this.stepProbability.getText());
            int count = Integer.parseInt(this.matrixCount.getText());
            List<Integer> sizes = Arrays.stream(this.matrixSize.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            LineChart<Number, Number> clusterCountChart = painter.paintEmptyLineChart(clusterCountChartPane, "Зависимость количество кластеров от концентрации");
            LineChart<Number, Number> clusterSizeChart = painter.paintEmptyLineChart(clusterSizeChartPane, "Средний размер кластеров");
            LineChart<Number, Number> redCellsAdded = painter.paintEmptyLineChart(redCellsCountLineChart, "Количество добавленых красных клеток");
            LineChart<Number, Number> wayLengths = painter.paintEmptyLineChart(wayLengthLineChart, "Средняя длина пути");
            LineChart<Number, Number> redCellsStationDistancesPiChart = painter.paintEmptyLineChart(objectStationDistance1, "Расстояние вычисляется с помощью теоремы Пифагора");
            LineChart<Number, Number> redCellsStationDistancesNePiChart = painter.paintEmptyLineChart(objectStationDistance2, "Расстояние вычисляется как количество переходов");
//            ForkJoinPool forkJoinPool = new ForkJoinPool(8);
//            for (Integer size : sizes) {
//                System.out.println("For size " + size + " generating start");
//                long startTimeForSize = System.currentTimeMillis();
//                List<LineChartNode> midClustersCounts = new ArrayList<>();
//                List<LineChartNode> midClustersSize = new ArrayList<>();
//                List<LineChartNode> midRedCellsCount = new ArrayList<>();
//                List<LineChartNode> midWayLengths = new ArrayList<>();
//                List<LineChartNode> redCellsStationDistancesPythagoras = new ArrayList<>();
//                List<LineChartNode> redCellsStationDistancesDiscrete = new ArrayList<>();
//                DoubleStream.iterate(0.01,
//                        x -> {
////                            if (x < 0.4)
////                                return x + 0.02;
////                            else if (x > 0.4 && x < 0.6) return x + 0.05;
//                            return x + 0.01;
//                        })
//                        .limit(100)
//                        .filter(x -> x < 1)
//                        .forEach(probability -> {
//                            RandomFillingType randomFillingType = new RandomFillingType();
//                            randomFillingType.setSize(size);
//                            randomFillingType.setPercolationProbability(probability);
//                            System.out.println("    Initializing for percolation probability " + probability + " started");
//                            long startTimeProbability = System.currentTimeMillis();
//                            List<Experiment> experiments = null;
//                            try {
//                                experiments = forkJoinPool.submit(() -> experimentManager.initializeExperiments(count, randomFillingType)).get();
//                            } catch (InterruptedException | ExecutionException e) {
//                                e.printStackTrace();
//                            }
////                            List<Experiment> experiments = experimentManager.initializeExperiments(count, randomFillingType);
////                    startTimeProbability = System.currentTimeMillis();
////                    System.out.println("        Collecting statistic started");
//                            midClustersCounts.add(new LineChartNode(probability, statManager.clusterCountStat(experiments)));
//                            midClustersSize.add(new LineChartNode(probability, statManager.clusterSizeStat(experiments)));
//                            midRedCellsCount.add(new LineChartNode(probability, statManager.redCellsCountStat(experiments)));
//                            midWayLengths.add(new LineChartNode(probability, statManager.wayLengthStat(experiments)));
//                            redCellsStationDistancesPythagoras.add(new LineChartNode(probability, statManager.redCellStationDistanceForPythagoras(experiments)));
//                            redCellsStationDistancesDiscrete.add(new LineChartNode(probability, statManager.redCellStationDistanceForDiscrete(experiments)));
////                    System.out.println("    Collecting statistic finished time=" + (System.currentTimeMillis() - startTimeProbability));
////                            System.out.println("    Initializing for percolation probability " + probability + " finished time=" + (System.currentTimeMillis() - startTimeProbability));
//                            logger.info("    Initializing for percolation probability " + probability + " finished time=" + (System.currentTimeMillis() - startTimeProbability));
//
//                        });
//                painter.addSeriesToLineChart(clusterCountChart, "Mat size " + size, midClustersCounts);
//                painter.addSeriesToLineChart(clusterSizeChart, "Mat size " + size, midClustersSize);
//                painter.addSeriesToLineChart(redCellsAdded, "Mat size " + size, midRedCellsCount);
//                painter.addSeriesToLineChart(wayLengths, "Mat size " + size, midWayLengths);
//                painter.addSeriesToLineChart(redCellsStationDistancesPiChart, "Mat size " + size, redCellsStationDistancesPythagoras);
//                painter.addSeriesToLineChart(redCellsStationDistancesNePiChart, "Mat size " + size, redCellsStationDistancesDiscrete);
//                logger.warn("For size " + size + " with " + count + " matrices generated time=" + (System.currentTimeMillis() - startTimeForSize));
//            }
            for (Integer size : sizes) {
                ObservableList<XYChart.Data> midClustersCounts = FXCollections.observableArrayList();
                ObservableList<XYChart.Data> midClustersSize = FXCollections.observableArrayList();
                ObservableList<XYChart.Data> midRedCellsCount = FXCollections.observableArrayList();
                ObservableList<XYChart.Data> midWayLengths = FXCollections.observableArrayList();
                ObservableList<XYChart.Data> redCellsStationDistancesPythagoras = FXCollections.observableArrayList();
                ObservableList<XYChart.Data> redCellsStationDistancesDiscrete = FXCollections.observableArrayList();
                defaultService.putMidClustersCounts(size, midClustersCounts);
                defaultService.putMidClustersSize(size, midClustersSize);
                defaultService.putMidRedCellsCount(size, midRedCellsCount);
                defaultService.putMidWayLengths(size, midWayLengths);
                defaultService.putRedCellsStationDistancesDiscrete(size, redCellsStationDistancesDiscrete);
                defaultService.putRedCellsStationDistancesPythagoras(size, redCellsStationDistancesPythagoras);
                painter.addObservableSeries(clusterCountChart, "Mat size " + size, midClustersCounts);
                painter.addObservableSeries(clusterSizeChart, "Mat size " + size, midClustersSize);
                painter.addObservableSeries(redCellsAdded, "Mat size " + size, midRedCellsCount);
                painter.addObservableSeries(wayLengths, "Mat size " + size, midWayLengths);
                painter.addObservableSeries(redCellsStationDistancesPiChart, "Mat size " + size, redCellsStationDistancesPythagoras);
                painter.addObservableSeries(redCellsStationDistancesNePiChart, "Mat size " + size, redCellsStationDistancesDiscrete);
                new Thread(() -> {
                    DoubleStream.iterate(0.01, x -> x + 0.01)
                            .limit(100)
                            .filter(x -> x <= 0.8500001)
                            .forEach(probability -> {
                                defaultService.consume(count, size, probability);
                            });
                }).start();
            }
        });
    }

        void paintByDistanceResolverAndCheckBox () {
            if (distanceCalculatorType.getText().equals(PYTHAGORAS)) {
                distanceCalculatorType.setText(DISCRETE);
                final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
                paintByCheckBox(experiment, PYTHAGORAS);
            } else if (distanceCalculatorType.getText().equals(DISCRETE)) {
                final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
                distanceCalculatorType.setText(PYTHAGORAS);
                paintByCheckBox(experiment, DISCRETE);
            }
        }

        void paintByCheckBox (Experiment experiment, String type){
            int tape = parseInt(tapeCount.getText());
            painter.paintCanvas(gridPane, experiment.getMatrix());
            if (tapeCheckBox.isSelected()) {
                painter.paintLightningBoltAndTape(lightningBoltPane, experiment.getPath(), experiment.generateTape(tape), experiment.getMatrix());
            } else {
                painter.paintLightningBoltAndRelations(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(type), experiment.getMatrix());
            }
        }

        int parseInt (String s){
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return 1;
            }
        }


    }