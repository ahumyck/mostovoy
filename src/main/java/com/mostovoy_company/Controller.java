package com.mostovoy_company;

import com.mostovoy_company.analyzer.AnalyzerManager;
import com.mostovoy_company.analyzer.TableViewAnalyzerData;
import com.mostovoy_company.chart.ChartsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


@Component
@FxmlView("sample.fxml")
@Slf4j
public class Controller {

    public Tab fullExperiment;

    private final FxWeaver fxWeaver;

    private AnalyzerManager analyzerManager;

    @FXML
    public Tab manualMatrixTab;
    @FXML
    public TextField matrixCountAnalyzer;
    @FXML
    public TextField concentrationAnalyzer;
    @FXML
    public TextField matrixSizeAnalyzer;
    @FXML
    public Button applyAnalyzerExperiment;
    @FXML
    public TableView<TableViewAnalyzerData> analyzerDataTable;


    public Controller(FxWeaver fxWeaver, AnalyzerManager analyzerManager) {
        this.fxWeaver = fxWeaver;
        this.analyzerManager = analyzerManager;
    }

    @FXML
    public void initialize() {
        tableViewInitializer();
        fullExperiment.setContent(fxWeaver.loadController(ChartsController.class).getContent());
        manualMatrixTab.setContent(fxWeaver.loadController(ManualMatrixController.class).getContent());
        applyAnalyzerExperiment.setOnAction(event -> {
            int matrixSize = Integer.parseInt(this.matrixSizeAnalyzer.getText());
            int numberOfMatrices = Integer.parseInt(this.matrixCountAnalyzer.getText());
            double probability = Double.parseDouble(this.concentrationAnalyzer.getText());
            ObservableList<TableViewAnalyzerData> analyzerDataObservableList = analyzerManager.initializeAnalyzerExperiments(numberOfMatrices, matrixSize, probability);
            analyzerDataTable.setItems(analyzerDataObservableList);
        });
    }

    void tableViewInitializer(){
        TableColumn<TableViewAnalyzerData, String> size = new TableColumn<>("L");
        TableColumn<TableViewAnalyzerData, String> probability = new TableColumn<>("конц");
        TableColumn<TableViewAnalyzerData, String> averageWhiteCellsPerColumn = new TableColumn<>("Среднее белых в столбце");
        TableColumn<TableViewAnalyzerData, String> minWhiteCellsPerColumn= new TableColumn<>("Минимальное белых в столбце");
        TableColumn<TableViewAnalyzerData, String> maxWhiteCellsPerColumn = new TableColumn<>("Максимальное белых в столбце");

        TableColumn<TableViewAnalyzerData, String> averageWhiteCellsPerRow = new TableColumn<>("Среднее белых в строке");
        TableColumn<TableViewAnalyzerData, String> minWhiteCellsPerRow = new TableColumn<>("Минимальное белых в строке");
        TableColumn<TableViewAnalyzerData, String> maxWhiteCellsPerRow = new TableColumn<>("Максимальное белых в строке");

        TableColumn<TableViewAnalyzerData, String> sumBlackCells = new TableColumn<>("Сумма черных по строкам");
        TableColumn<TableViewAnalyzerData, String> emptyRows = new TableColumn<>("Пустые строки");
        TableColumn<TableViewAnalyzerData, String> averageBlackCells = new TableColumn<>("Среднее черных в строке");


        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        probability.setCellValueFactory(new PropertyValueFactory<>("probability"));

        averageWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("averageWhiteCellsPerColumns"));
        minWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("minWhiteCellsPerColumn"));
        maxWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("maxWhiteCellsPerColumn"));

        averageWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("averageWhiteCellsPerRow"));
        minWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("minWhiteCellsPerRow"));
        maxWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("maxWhiteCellsPerRow"));

        sumBlackCells.setCellValueFactory(new PropertyValueFactory<>("sumBlackCell"));
        emptyRows.setCellValueFactory(new PropertyValueFactory<>("emptyRows"));
        averageBlackCells.setCellValueFactory(new PropertyValueFactory<>("averageBlackCells"));

        analyzerDataTable.setItems(analyzerManager.initializeAnalyzerExperiments(10, 30, 0.1));
        analyzerDataTable.getColumns().addAll(size, probability,
                averageWhiteCellsPerColumn, minWhiteCellsPerColumn, maxWhiteCellsPerColumn,
                averageWhiteCellsPerRow, minWhiteCellsPerRow, maxWhiteCellsPerRow,
                sumBlackCells, emptyRows, averageBlackCells);
    }
}