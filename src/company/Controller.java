package company;

import company.expirement.Experiment;
import company.expirement.ExperimentManager;
import company.entity.Matrix;
import company.filling.*;
import company.filling.customs.CustomTestFillingType;
import company.filling.customs.MaltTestFillingType;
import company.paint.LineChartNode;
import company.paint.Painter;
import company.stat.StatManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ExperimentManager experimentManager = new ExperimentManager();
    private Painter painter = new Painter();
    private StatManager statManager = new StatManager();

    @FXML
    private ListView<Experiment> experimentListView;

    @FXML
    private AnchorPane grid;

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
    public AnchorPane clusterCountChart;

    @FXML
    public AnchorPane clusterSizeChart;

    @FXML
    public void initialize() {
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(FXCollections.observableArrayList(new RandomFillingType(), new MaltTestFillingType("Мальтийский крест")));
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        experimentListView.setOnMouseClicked(item -> {
            Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            Matrix matrix = experimentManager.getMatrix(experiment);
//            System.out.println(experiment.getPath() + "\n" + matrix.toString());
            painter.paintCanvas(grid, matrix);
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
            } else if (fillingType instanceof MaltTestFillingType) {
                experimentListView.setItems(experimentManager.initializeExperiments(number, fillingType));
            }
        });
        applyExperiment.setOnAction(event -> {
            double startProbability = Double.valueOf(this.startProbability.getText());
            double endProbability = Double.valueOf(this.endProbability.getText());
            double stepProbability = Double.valueOf(this.stepProbability.getText());
            int count = Integer.valueOf(this.matrixCount.getText());
            int size = Integer.valueOf(this.matrixSize.getText());
            List<LineChartNode> midClustersCounts = new ArrayList<>();
            List<LineChartNode> midClustersSize = new ArrayList<>();
            for (double propability  = startProbability; propability <= endProbability; propability += stepProbability)
            {
                System.out.println("propability: " + propability);
                RandomFillingType randomFillingType = new RandomFillingType();
                randomFillingType.setSize(size);
                randomFillingType.setPercolationProbability(propability);
                List<Matrix> matrices = experimentManager.getMatrices(count, randomFillingType);
                midClustersCounts.add(new LineChartNode(propability, statManager.clusterCountStat(matrices)));
                midClustersSize.add(new LineChartNode(propability, statManager.clusterSizeStat(matrices)));
            }
            System.out.println(midClustersCounts);
            painter.paintLineChart(clusterCountChart, midClustersCounts, "Среднее количество кластеров");
            painter.paintLineChart(clusterSizeChart, midClustersSize, "Средний размер кластеров");
        });
    }


}