package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.chart_experiment.ExperimentManager;
import com.mostovoy_company.expirement.chart_experiment.MatrixBuilderByPicture;
import com.mostovoy_company.expirement.chart_experiment.entity.Experiment;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.expirement.chart_experiment.filling.FillingType;
import com.mostovoy_company.expirement.chart_experiment.filling.RandomFillingType;
import com.mostovoy_company.expirement.chart_experiment.filling.customs.CustomTestFillingType;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
    public StackPane lightningBoltPane;
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
    public Button saveMatrixButton;
    @FXML
    public Button uploadMatrixButton;
    @FXML
    public Button uploadPictureButton;


    private ObservableList<FillingType> fillingTypesList;
    private ExperimentManager experimentManager;
    private Painter painter;
    private FxWeaver fxWeaver;
    private MatrixBuilderByPicture pictureBuilder;

    public ManualMatrixController(List<FillingType> fillingTypesList,
                                  ExperimentManager experimentManager,
                                  Painter painter, FxWeaver fxWeaver, MatrixBuilderByPicture pictureBuilder) {
        this.fillingTypesList = FXCollections.observableArrayList(fillingTypesList);
        this.experimentManager = experimentManager;
        this.painter = painter;
        this.fxWeaver = fxWeaver;
        this.pictureBuilder = pictureBuilder;
    }

    Node getContent() {
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
        clearListButton.setOnAction(actionEvent -> experimentListView.getItems().clear());
        mainTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintExperiment(experiment);
        });
        mainTabPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            if (experiment != null)
                paintExperiment(experiment);
        });
        experimentNumber.setText("1");

        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintExperiment(experiment);
            showMatrixInfo(experiment);
        });

        saveMatrixButton.setOnAction(actionEvent ->
                fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter)
                        .ifPresent(file -> {
                            Experiment selectedItem = experimentListView.getSelectionModel().getSelectedItem();
                            if (selectedItem != null) {
                                selectedItem.saveExperimentToJson(file.getPath());
                            }
                        })
        );
        uploadMatrixButton.setOnAction(actionEvent ->
                fxWeaver.loadController(FileChooserController.class).getMultipleFiles(FileChooserController.jsonExtensionFilter)
                        .ifPresent(files -> experimentListView.getItems().addAll(loadExperiments(files)))
        );

        uploadPictureButton.setOnAction(action -> fxWeaver
                .loadController(FileChooserController.class)
                .getSingleFile(FileChooserController.pngExtensionFilter)
                .ifPresent(file -> {
                    try {
                        BufferedImage image = ImageIO.read(file);
                        Matrix matrix = pictureBuilder.build(image);
                        Experiment experiment = experimentManager.initializeExperiment(matrix, file.getName());
                        experimentListView.getItems().add(experiment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));


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
                if (number != 1)
                    experimentListView.getItems().addAll(experimentManager.initializeExperimentsParallel(number, fillingType));
                else
                    experimentListView.getItems().add(experimentManager.initializeSingleExperiment(fillingType));
            } else if (fillingType instanceof CustomTestFillingType) {
                experimentListView.getItems().addAll(experimentManager.initializeExperimentsParallel(number, fillingType));
            }
        });
    }

    private void paintExperiment(Experiment experiment) {
        painter.paintCanvas(gridPane,
                experiment.getMatrix(),
                Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 30);
        painter.paintLightningBoltAndRelations(lightningBoltPane,
                experiment.getPercolationWay(),
                experiment.getProgrammings(),
                experiment.getMatrix(),
                Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 30);
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
        addLabelToMatrixInfo("Среднее расстояние установки: " + String.format("%.2f", statistic.getPythagorasDistance().getFirst()));
        addLabelToMatrixInfo("Среднее черных клеток в пределах перколяции: " + String.format("%.2f", statistic.getAverageBlackCellsPerRowInWayWidth()));
        addLabelToMatrixInfo("Среднее темнокрасных клеток в пределах перколяции: " + String.format("%.2f", statistic.getAverageRedCellsPerRowInTapeWidth()));
    }

    private void addLabelToMatrixInfo(String text) {
        Label label = new Label();
        label.setFont(new Font(14.0));
        label.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
        label.setText(text);
        matrixInfo.getChildren().add(label);
    }

    private ObservableList<Experiment> loadExperiments(List<File> files) {
        ObservableList<Experiment> matrixObservableList = FXCollections.observableArrayList();
        for (File file : files) {
            try {
                Experiment experiment = Experiment.getExperimentFromJson(file.getPath());
                experiment.calculateProgrammingPercolation();
                matrixObservableList.add(experiment);
            } catch (IOException ignored) {
            }
        }
        return matrixObservableList;
    }
}
