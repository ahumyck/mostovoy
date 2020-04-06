package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.filling.*;
import company.filling.customs.*;
import company.paint.LineChartNode;
import company.paint.Painter;
import company.stat.NormalizedStatManager;
import company.stat.StatManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static company.programminPercolation.DistancePercolationTypeResolver.NE_PIFAGOR;
import static company.programminPercolation.DistancePercolationTypeResolver.PIFAGOR;

public class Controller {

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
    public void initialize() {
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
            painter.paintCanvas(gridPane, experiment.getMatrix());
            painter.paintLightningBoltCanvas(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(distanceCalculatorType.getText()), experiment.getMatrix());
            currentClustersCount.setText("Количество кластеров: " + experiment.getMatrix().getClusterCounter());
            redCellsLabel.setText("Красных клеток: " + experiment.getRedCellsCounter());
            shortestPathLabel.setText("Расстояние: " + experiment.getDistance());
        });
        distanceCalculatorType.setOnAction(actionEvent -> {
            if (distanceCalculatorType.getText().equals(PIFAGOR)) {
                final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
                distanceCalculatorType.setText(NE_PIFAGOR);
                painter.paintLightningBoltCanvas(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(distanceCalculatorType.getText()), experiment.getMatrix());
            } else if (distanceCalculatorType.getText().equals(NE_PIFAGOR)) {
                final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
                distanceCalculatorType.setText(PIFAGOR);
                painter.paintLightningBoltCanvas(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(distanceCalculatorType.getText()), experiment.getMatrix());
            }

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
            LineChart<Number, Number> redCellsStationDistancesPiChart = painter.paintEmptyLineChart(objectStationDistance1, "Пифагор");
            LineChart<Number, Number> redCellsStationDistancesNePiChart = painter.paintEmptyLineChart(objectStationDistance2, "Не пифагор");
            ForkJoinPool forkJoinPool = new ForkJoinPool(4);
            for (Integer size : sizes) {
                System.out.println("For size " + size + " generating start");
                long startTimeForSize = System.currentTimeMillis();
                List<LineChartNode> midClustersCounts = new ArrayList<>();
                List<LineChartNode> midClustersSize = new ArrayList<>();
                List<LineChartNode> midRedCellsCount = new ArrayList<>();
                List<LineChartNode> midWayLengths = new ArrayList<>();
                List<LineChartNode> redCellsStationDistancesPi = new ArrayList<>();
                List<LineChartNode> redCellsStationDistancesNePi = new ArrayList<>();
                DoubleStream.iterate(0.02,
                        x -> {
                            return x + 0.1;
                        })
                        .limit(100)
                        .filter(x -> x < 1)
                        .forEach(probability -> {
                            RandomFillingType randomFillingType = new RandomFillingType();
                            randomFillingType.setSize(size);
                            randomFillingType.setPercolationProbability(probability);
                            System.out.println("    Initializing for percolation probability " + probability + " started");
                            long startTimePropability = System.currentTimeMillis();
                            List<Experiment> experiments = null;
                            try {
                                experiments = forkJoinPool.submit(() -> experimentManager.initializeExperiments(count, randomFillingType)).get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
//                            List<Experiment> experiments = experimentManager.initializeExperiments(count, randomFillingType);
//                    startTimePropability = System.currentTimeMillis();
//                    System.out.println("        Collecting statistic started");
                            midClustersCounts.add(new LineChartNode(probability, statManager.clusterCountStat(experiments)));
                            midClustersSize.add(new LineChartNode(probability, statManager.clusterSizeStat(experiments)));
                            midRedCellsCount.add(new LineChartNode(probability, statManager.redCellsCountStat(experiments)));
                            midWayLengths.add(new LineChartNode(probability, statManager.wayLengthStat(experiments)));
//                            redCellsStationDistancesPi.add(new LineChartNode(probability, statManager.redCellStationDistanceForPifagor(experiments)));
//                            redCellsStationDistancesNePi.add(new LineChartNode(probability, statManager.redCellStationDistanceForNePifagor(experiments)));
//                    System.out.println("    Collecting statistic finished time=" + (System.currentTimeMillis() - startTimePropability));
                            System.out.println("    Initializing for percolation probability " + probability + " finished time=" + (System.currentTimeMillis() - startTimePropability));

                        });
                painter.addSeriesToLineChart(clusterCountChart, "Mat size " + size, midClustersCounts);
                painter.addSeriesToLineChart(clusterSizeChart, "Mat size " + size, midClustersSize);
                painter.addSeriesToLineChart(redCellsAdded, "Mat size " + size, midRedCellsCount);
                painter.addSeriesToLineChart(wayLengths, "Mat size " + size, midWayLengths);
                painter.addSeriesToLineChart(redCellsStationDistancesPiChart, "Mat size " + size, redCellsStationDistancesPi);
                painter.addSeriesToLineChart(redCellsStationDistancesNePiChart, "Mat size " + size, redCellsStationDistancesNePi);
                System.out.println("For size " + size + " generated time=" + (System.currentTimeMillis() - startTimeForSize));
            }
        });
    }

    private List<Double> generateProbabilities(double start, double end, double step) {
        List<Double> probabilities = new ArrayList<>();
        while (start <= end) {
            probabilities.add(start);
            start += step;
        }
        return probabilities;
    }


}