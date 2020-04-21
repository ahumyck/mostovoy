package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.entity.Matrix;
import com.mostovoy_company.expirement.entity.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.entity.Statistic;
import com.mostovoy_company.expirement.filling.FillingType;
import com.mostovoy_company.expirement.filling.RandomFillingType;
import com.mostovoy_company.expirement.filling.customs.CustomTestFillingType;
import com.mostovoy_company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import lombok.var;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.mostovoy_company.expirement.programminPercolation.distance.DistanceCalculatorTypeResolver.DISCRETE;
import static com.mostovoy_company.expirement.programminPercolation.distance.DistanceCalculatorTypeResolver.PYTHAGORAS;

@FxmlView("manualMatrix.fxml")
@Component
public class ManualMatrixController {

    @FXML
    public TextField experimentNumber;
    @FXML
    public Label gridSizeLabel;
    @FXML
    public TextField fillingProbability;
    @FXML
    public Label probabilityLabel;
    @FXML
    public ComboBox<FillingType> fillingTypes;
    @FXML
    public Button applyConfiguration;
    @FXML
    public ComboBox<GridSize> gridSize;
    @FXML
    public ListView<Experiment> experimentListView;
    @FXML
    public Tab gridTab;
    @FXML
    public Tab lightningBoltTab;
    @FXML
    public Canvas lightningBoltPane;
    @FXML
    public Button distanceCalculatorType;
    @FXML
    public HBox manualMatrixAnchorPane;
    @FXML
    public TabPane mainTabPane;
    @FXML
    public Canvas gridPane;
    @FXML
    public HBox experimentListAndCanvas;
    @FXML
    public VBox matrixInfo;


    private ObservableList<FillingType> fillingTypesList;
    private ExperimentManager experimentManager;
    private Painter painter;


    public ManualMatrixController(List<FillingType> fillingTypesList,
                                  ExperimentManager experimentManager,
                                  Painter painter) {
        this.fillingTypesList = FXCollections.observableArrayList(fillingTypesList);
        this.experimentManager = experimentManager;
        this.painter = painter;
    }

    public Node getContent() {
        return this.manualMatrixAnchorPane;
    }

    private String filepath = System.getProperty("user.dir") + "/src/main/resources/matrix/";

    @FXML
    public void initialize() {
        HBox.setHgrow(experimentListAndCanvas, Priority.ALWAYS);
        HBox.setHgrow(mainTabPane, Priority.ALWAYS);
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(fillingTypesList);
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        mainTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintByCheckBox(experiment, PYTHAGORAS);
        });
        mainTabPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintByCheckBox(experiment, PYTHAGORAS);
        });


        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
            showMatrixInfo(experiment);
//            try {
//                experiment.getMatrix().toJSON(filepath + experiment.toString());
//                System.out.println("saving file: " + filepath + experiment.toString());
//            } catch (IOException e) {
//                System.out.println("Unable to save matrix: " + filepath + experiment.toString());
//            }
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
//            loadFiles();
            String txt = experimentNumber.getText();
            int number = Integer.parseInt(txt);
            FillingType fillingType = fillingTypes.getValue();
            if (fillingType instanceof RandomFillingType) {
                int size = gridSize.getValue().getValue();
                double probability = Double.parseDouble(fillingProbability.getText());
                ((RandomFillingType) fillingType).setPercolationProbability(probability);
                ((RandomFillingType) fillingType).setSize(size);
                experimentListView.setItems(FXCollections.observableArrayList(experimentManager.initializeExperimentsParallel(number, fillingType)));
            } else if (fillingType instanceof CustomTestFillingType) {
                experimentListView.setItems(FXCollections.observableArrayList(experimentManager.initializeExperimentsParallel(number, fillingType)));
            }
        });
    }

    void paintByCheckBox(Experiment experiment, String type) {
        painter.paintCanvas(gridPane, experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 20);
        painter.paintLightningBoltAndRelations(lightningBoltPane, experiment.getPercolationWay(), experiment.getProgrammings(type), experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 60);
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

    private void showMatrixInfo(Experiment experiment) {
        matrixInfo.getChildren().clear();
        Statistic statistic = experiment.getExperimentStatistic();
        addLabelToMatrixInfo("Количество кластеров: " + statistic.getClusterCount());
        addLabelToMatrixInfo("Количество черных клеток: " + statistic.getBlackCellCount());
        addLabelToMatrixInfo("Количество красных клеток: " + statistic.getRedCellCount());
        addLabelToMatrixInfo("Длина перколяционного пути: " + statistic.getPercolationWayLength());
        addLabelToMatrixInfo("Ширина перколяционного пути: " + statistic.getPercolationWayWidth());
        addLabelToMatrixInfo("Средний размер межластерного интервала: " + statistic.getMidInterClustersInterval());
        addLabelToMatrixInfo("Количество межкластреных дырок: " + statistic.getInterClustersHoleCount());
    }

    private void addLabelToMatrixInfo(String text){
        var label = new Label();
        label.setFont(new Font(14.0));
        label.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
        label.setText(text);
        matrixInfo.getChildren().add(label);
    }

    void loadFiles() {
        ObservableList<Experiment> matrixObservableList = FXCollections.observableArrayList();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String currentFilename = filepath + "Эксперимент №" + i;
            try {
                Matrix matrix = Matrix.fromJSON(currentFilename);
                Experiment experiment = new Experiment().name("Эксперимент №" + i).matrix(matrix).clusterization();
                matrixObservableList.add(experiment);
            } catch (IOException e) {
                break;
            }
        }
        experimentListView.setItems(matrixObservableList);
    }
}
