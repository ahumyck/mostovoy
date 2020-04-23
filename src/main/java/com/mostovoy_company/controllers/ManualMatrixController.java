package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.entity.Experiment;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.var;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    @FXML
    public Button clearListButton;
    @FXML
    public Button saveButton;
    @FXML
    public Button uploadButton;


    private ObservableList<FillingType> fillingTypesList;
    private ExperimentManager experimentManager;
    private Painter painter;
    private FxWeaver fxWeaver;
    private String filepath = System.getProperty("user.dir") + "/src/main/resources/matrix/";

    public ManualMatrixController(List<FillingType> fillingTypesList,
                                  ExperimentManager experimentManager,
                                  Painter painter, FxWeaver fxWeaver) {
        this.fillingTypesList = FXCollections.observableArrayList(fillingTypesList);
        this.experimentManager = experimentManager;
        this.painter = painter;
        this.fxWeaver = fxWeaver;
    }

    public Node getContent() {
        return this.manualMatrixAnchorPane;
    }

    @FXML
    public void initialize() {
        experimentListView.setItems(FXCollections.observableArrayList());
        HBox.setHgrow(experimentListAndCanvas, Priority.ALWAYS);
        HBox.setHgrow(mainTabPane, Priority.ALWAYS);
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(fillingTypesList);
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        clearListButton.setOnAction(actionEvent -> {
            experimentListView.getItems().clear();
        });
        mainTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintByCheckBox(experiment);
        });
        mainTabPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintByCheckBox(experiment);
        });
        experimentNumber.setText("1");

        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment);
            showMatrixInfo(experiment);
        });

        saveButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter)
                    .ifPresent(file -> {
                        Experiment selectedItem = experimentListView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            selectedItem.saveExperimentToJson(file.getPath());
                        }
                    });
        });
        uploadButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getMultipleFiles(FileChooserController.jsonExtensionFilter)
                    .ifPresent(files -> experimentListView.getItems().addAll(loadExperiments(files)));
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
                experimentListView.getItems().addAll(experimentManager.initializeExperimentsParallel(number, fillingType));
            } else if (fillingType instanceof CustomTestFillingType) {
                experimentListView.getItems().addAll(experimentManager.initializeExperimentsParallel(number, fillingType));
            }
        });
    }

    void paintByCheckBox(Experiment experiment) {
        painter.paintCanvas(gridPane, experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 30);
        painter.paintLightningBoltAndRelations(lightningBoltPane, experiment.getPercolationWay(), experiment.getProgrammings(PYTHAGORAS), experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 30);
    }

    private void showMatrixInfo(Experiment experiment) {
        matrixInfo.getChildren().clear();
        Statistic statistic = experiment.getExperimentStatistic();
        addLabelToMatrixInfo("Количество кластеров: " + statistic.getClusterCount());
        addLabelToMatrixInfo("Количество черных клеток: " + statistic.getBlackCellCount());
        addLabelToMatrixInfo("Количество красных клеток: " + statistic.getRedCellCount());
        addLabelToMatrixInfo("Длина перколяционного пути: " + statistic.getPercolationWayLength());
        addLabelToMatrixInfo("Ширина перколяционного пути: " + statistic.getPercolationWayWidth());
        addLabelToMatrixInfo("Средний размер межластерного интервала: " + String.format("%.2f", statistic.getMidInterClustersInterval()));
        addLabelToMatrixInfo("Количество межкластреных дырок: " + statistic.getInterClustersHoleCount());
    }

    private void addLabelToMatrixInfo(String text) {
        var label = new Label();
        label.setFont(new Font(14.0));
        label.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
        label.setText(text);
        matrixInfo.getChildren().add(label);
    }

    private ObservableList<Experiment> loadExperiments(List<File> files) {
        ObservableList<Experiment> matrixObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try {
                Experiment experiment = Experiment.getExperimentFromJson(file.getPath());
                matrixObservableList.add(experiment);
            } catch (IOException ignored) {
            }
        }
        return matrixObservableList;
    }
}
