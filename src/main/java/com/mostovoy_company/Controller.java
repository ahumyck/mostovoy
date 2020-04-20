package com.mostovoy_company;

import com.mostovoy_company.table_view.analyzer.AnalyzerManager;
import com.mostovoy_company.table_view.TableViewAnalyzerData;
import com.mostovoy_company.table_view.TableViewController;
import com.mostovoy_company.chart.ChartsController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        TableViewController.initialize(analyzerDataTable);
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
}