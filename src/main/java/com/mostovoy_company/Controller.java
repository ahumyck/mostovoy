package com.mostovoy_company;

import com.mostovoy_company.analyzer.AnalyzerData;
import com.mostovoy_company.analyzer.AnalyzerManager;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//import com.mostovoy_company.stat.NormalizedStatManager;

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
    public TableView<AnalyzerData> analyzerDataTable;


    public Controller(FxWeaver fxWeaver, AnalyzerManager analyzerManager) {
        this.fxWeaver = fxWeaver;
        this.analyzerManager = analyzerManager;
    }

    @FXML
    public void initialize() {
        tableViewInitializer();
//        tapeCheckBox.setSelected(false);
        fullExperiment.setContent(fxWeaver.loadController(ChartsController.class).getStatisticCharts());
        manualMatrixTab.setContent(fxWeaver.loadController(ManualMatrixController.class).getContent());
        applyAnalyzerExperiment.setOnAction(event -> {
            int matrixSize = Integer.parseInt(this.matrixSizeAnalyzer.getText());
            int numberOfMatrices = Integer.parseInt(this.matrixCountAnalyzer.getText());
            double probability = Double.parseDouble(this.concentrationAnalyzer.getText());
            ObservableList<AnalyzerData> analyzerDataObservableList = analyzerManager.initializeAnalyzerExperiments(numberOfMatrices, matrixSize, probability);
            analyzerDataTable.setItems(analyzerDataObservableList);
        });
    }

    void tableViewInitializer(){
        TableColumn<AnalyzerData, String> size = new TableColumn<>("L");
        TableColumn<AnalyzerData, String> probability = new TableColumn<>("конц");
        TableColumn<AnalyzerData, String> blackCellsPerColumn = new TableColumn<>("Черных клеток в столбце");
        TableColumn<AnalyzerData, String> blackCellsPerRow = new TableColumn<>("Черных клеток в строке");
        TableColumn<AnalyzerData, String> blackCellsAveragePerColumn = new TableColumn<>("Срдн в стлбц");
        TableColumn<AnalyzerData, String> blackCellsAveragePerRow = new TableColumn<>("Срдн в стрк");
        TableColumn<AnalyzerData, String> emptyRows = new TableColumn<>("Пуст стрк");
        TableColumn<AnalyzerData, String> emptyColumns = new TableColumn<>("Пуст стлбц");
        TableColumn<AnalyzerData, String> totalBlackCells = new TableColumn<>("Всг чк");

        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        size.setMinWidth(35);
        size.setMaxWidth(35);

        probability.setCellValueFactory(new PropertyValueFactory<>("probability"));
        size.setMinWidth(60);
        size.setMaxWidth(60);

        blackCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("blackCellsPerColumn"));
        blackCellsPerColumn.setMinWidth(200);

        blackCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("blackCellsPerRow"));
        blackCellsPerRow.setMinWidth(200);

        blackCellsAveragePerColumn.setCellValueFactory(new PropertyValueFactory<>("blackCellsAveragePerColumn"));
        blackCellsAveragePerColumn.setMinWidth(75);
        blackCellsAveragePerColumn.setMaxWidth(75);

        blackCellsAveragePerRow.setCellValueFactory(new PropertyValueFactory<>("blackCellsAveragePerRow"));
        blackCellsAveragePerRow.setMinWidth(75);
        blackCellsAveragePerRow.setMaxWidth(75);

        emptyRows.setCellValueFactory(new PropertyValueFactory<>("emptyRows"));
        emptyRows.setMinWidth(50);
        emptyRows.setMaxWidth(50);

        emptyColumns.setCellValueFactory(new PropertyValueFactory<>("emptyColumns"));
        emptyColumns.setMinWidth(50);
        emptyColumns.setMaxWidth(50);

        totalBlackCells.setCellValueFactory(new PropertyValueFactory<>("totalBlackCells"));
        totalBlackCells.setMinWidth(70);
        totalBlackCells.setMaxWidth(100);

        analyzerDataTable.setItems(FXCollections.emptyObservableList());
        analyzerDataTable.getColumns().addAll(size, probability,
                blackCellsPerColumn, blackCellsPerRow,
                blackCellsAveragePerRow, blackCellsAveragePerColumn,
                emptyRows, emptyColumns, totalBlackCells);


    }



    int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}