package com.mostovoy_company;

import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.filling.customs.CustomTestFillingType;
import com.mostovoy_company.paint.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.DISCRETE;
import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.PYTHAGORAS;

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
    public Label currentClustersCount;
    @FXML
    public Tab lightningBoltTab;
    @FXML
    public Label redCellsLabel;
    @FXML
    public Label shortestPathLabel;
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
    public Label currentBlackCellsCount;
    @FXML
    public Label currentWhiteCellsCount;


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

    public Node getContent(){
        return this.manualMatrixAnchorPane;
    }

    @FXML
    public void initialize() {
//        tapeCheckBox.setSelected(false);
        HBox.setHgrow(experimentListAndCanvas, Priority.ALWAYS);
        HBox.setHgrow(mainTabPane, Priority.ALWAYS);
        gridSize.setItems(FXCollections.observableArrayList(GridSize.values()));
        fillingTypes.setItems(fillingTypesList);
        experimentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mainTabPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("listened");
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
        });
        mainTabPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("listened");

            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
        });
        experimentListView.setOnMouseClicked(item -> {
            final Experiment experiment = experimentListView.getSelectionModel().getSelectedItem();
            paintByCheckBox(experiment, PYTHAGORAS);
            currentClustersCount.setText("Количество кластеров: " + experiment.getMatrix().getClusterCounter());
            currentBlackCellsCount.setText("Количество черных клеток: " + experiment.getMatrix().getCountOfBlackCells());
//            currentWhiteCellsCount.setText("Количество белых клеток: " + experiment.getMatrix().getCountOfWhiteCells());
            redCellsLabel.setText("Красных клеток: " + experiment.getStatistic().getRedCellCount());
            shortestPathLabel.setText("Расстояние: " + experiment.getDistance());
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
    }

    void paintByCheckBox(Experiment experiment, String type) {
//        int tape = parseInt(tapeCount.getText());
        painter.paintCanvas(gridPane, experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 60);
//        if (tapeCheckBox.isSelected()) {
//            painter.paintLightningBoltAndTape(lightningBoltPane, experiment.getPath(), experiment.generateTape(tape),experiment.getProgrammings(type), experiment.getMatrix());
//        } else {
        painter.paintLightningBoltAndRelations(lightningBoltPane, experiment.getPath(), experiment.getProgrammings(type), experiment.getMatrix(), Math.min(mainTabPane.getWidth(), mainTabPane.getHeight()) - 60);
//        }
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
}
