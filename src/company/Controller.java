package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.entity.Matrix;
import company.filling.*;
import company.filling.customs.*;
import company.paint.LineChartNode;
import company.paint.Painter;
import company.stat.StatManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private ExperimentManager experimentManager = new ExperimentManager();
    private final Painter painter = new Painter();
    private StatManager statManager = new StatManager();

    @FXML
    public Label currentClustersCount;

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
    public TextField startProbability;

    @FXML
    public TextField endProbability;

    @FXML
    public TextField stepProbability;

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
    public void initialize() {
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(),
                new MaltTestFillingType(),
                new HorizontalLineFillingType(),
                new SquareFillingType(),
                new VerticalLineFillingType()));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            painter.paintCanvas(gridPane, experiment.getMatrix());
            painter.paintLightningBoltCanvas(lightningBoltPane, experiment.getPath(), experiment.getMatrix());
            currentClustersCount.setText("Количество кластеров: " + experiment.getMatrix().getClusterCounter());
            redCellsLabel.setText("Красных клеток: " + experiment.getRedCellsCounter());
            shortestPathLabel.setText("Расстояние: " + experiment.getDistance());
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
                experimentListView.setItems(experimentManager.initializeExperiments(number, fillingType));
            } else if (fillingType instanceof CustomTestFillingType) {
                experimentListView.setItems(experimentManager.initializeExperiments(number, fillingType));
            }
        });
        applyExperiment.setOnAction(event -> {
            double startProbability = Double.parseDouble(this.startProbability.getText());
            double endProbability = Double.parseDouble(this.endProbability.getText());
            double stepProbability = Double.parseDouble(this.stepProbability.getText());
            int count = Integer.parseInt(this.matrixCount.getText());
            List<Integer> sizes = Arrays.stream(this.matrixSize.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            LineChart<Number, Number> clusterCountChart = painter.paintEmptyLineChart(clusterCountChartPane, "Среднее количество кластеров");
            LineChart<Number, Number> clusterSizeChart = painter.paintEmptyLineChart(clusterSizeChartPane, "Средний размер кластеров");
            for (Integer size : sizes) {
                List<LineChartNode> midClustersCounts = new ArrayList<>();
                List<LineChartNode> midClustersSize = new ArrayList<>();
                for (double probability = startProbability; probability <= endProbability + stepProbability; probability += stepProbability) {
                    RandomFillingType randomFillingType = new RandomFillingType();
                    randomFillingType.setSize(size);
                    randomFillingType.setPercolationProbability(probability);
                    List<Matrix> matrices = experimentManager.getMatrices(count, randomFillingType);
                    midClustersCounts.add(new LineChartNode(probability, statManager.clusterCountStat(matrices)));
                    midClustersSize.add(new LineChartNode(probability, statManager.clusterSizeStat(matrices)));
                }
                painter.addSeriesToLineChart(clusterCountChart, "Mat size " + size, midClustersCounts);
                painter.addSeriesToLineChart(clusterSizeChart, "Mat size " + size, midClustersSize);
                System.out.println("chart");
            }
        });
    }


}