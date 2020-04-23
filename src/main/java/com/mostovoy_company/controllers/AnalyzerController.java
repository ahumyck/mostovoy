package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.table_view.TableViewAnalyzerData;
import com.mostovoy_company.expirement.table_view.TableViewAnalyzerDataRepository;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerDataRepository;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@FxmlView("analyzer.fxml")
public class AnalyzerController {

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
    @FXML
    public HBox analyzer;
    @FXML
    public Button saveButton;
    @FXML
    public Button uploadButton;

    private AnalyzerManager analyzerManager;
    private FxWeaver fxWeaver;
    private TableViewAnalyzerDataRepository dataRepository = new TableViewAnalyzerDataRepository();

    public AnalyzerController(AnalyzerManager analyzerManager, FxWeaver fxWeaver) {
        this.analyzerManager = analyzerManager;
        this.fxWeaver = fxWeaver;
    }

    public Node getContent() {
        return this.analyzer;
    }


    @FXML
    public void initialize() {
//        HBox.setHgrow(analyzerDataTable, Priority.ALWAYS);
//        analyzerDataTable.setColumnResizePolicy((param) -> true);
        HBox.setHgrow(analyzerDataTable, Priority.ALWAYS);
        TableColumn<TableViewAnalyzerData, String> sizeColumn = new TableColumn<>("L");
        TableColumn<TableViewAnalyzerData, String> probabilityColumn = new TableColumn<>("Концентрация");
        TableColumn<TableViewAnalyzerData, String> averageWhiteCellsPerColumn = new TableColumn<>("Среднее белых в столбце");
        TableColumn<TableViewAnalyzerData, String> minWhiteCellsPerColumn = new TableColumn<>("Минимальное белых в столбце");
        TableColumn<TableViewAnalyzerData, String> maxWhiteCellsPerColumn = new TableColumn<>("Максимальное белых в столбце");

        TableColumn<TableViewAnalyzerData, String> averageWhiteCellsPerRow = new TableColumn<>("Среднее белых в строке");
        TableColumn<TableViewAnalyzerData, String> minWhiteCellsPerRow = new TableColumn<>("Минимальное белых в строке");
        TableColumn<TableViewAnalyzerData, String> maxWhiteCellsPerRow = new TableColumn<>("Максимальное белых в строке");

        TableColumn<TableViewAnalyzerData, String> sumBlackCells = new TableColumn<>("Сумма черных по строкам");
        TableColumn<TableViewAnalyzerData, String> emptyRows = new TableColumn<>("Пустые строки");
        TableColumn<TableViewAnalyzerData, String> averageBlackCells = new TableColumn<>("Среднее черных в строке");


        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("probability"));

        averageWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("averageWhiteCellsPerColumns"));
        minWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("minWhiteCellsPerColumn"));
        maxWhiteCellsPerColumn.setCellValueFactory(new PropertyValueFactory<>("maxWhiteCellsPerColumn"));

        averageWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("averageWhiteCellsPerRow"));
        minWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("minWhiteCellsPerRow"));
        maxWhiteCellsPerRow.setCellValueFactory(new PropertyValueFactory<>("maxWhiteCellsPerRow"));

        sumBlackCells.setCellValueFactory(new PropertyValueFactory<>("sumBlackCell"));
        emptyRows.setCellValueFactory(new PropertyValueFactory<>("emptyRows"));
        averageBlackCells.setCellValueFactory(new PropertyValueFactory<>("averageBlackCells"));

        analyzerDataTable.setItems(FXCollections.observableArrayList());
        analyzerDataTable.getColumns().clear();
        analyzerDataTable.getColumns().addAll(sizeColumn, probabilityColumn,
                averageWhiteCellsPerColumn, minWhiteCellsPerColumn, maxWhiteCellsPerColumn,
                averageWhiteCellsPerRow, minWhiteCellsPerRow, maxWhiteCellsPerRow,
                sumBlackCells, emptyRows, averageBlackCells);
        applyAnalyzerExperiment.setOnAction(event -> {
            int matrixSize = Integer.parseInt(this.matrixSizeAnalyzer.getText());
            int numberOfMatrices = Integer.parseInt(this.matrixCountAnalyzer.getText());
            double probability = Double.parseDouble(this.concentrationAnalyzer.getText());

            AnalyzerDataRepository analyzerDataRepository = analyzerManager.initializeAnalyzerExperiments(numberOfMatrices, matrixSize, probability);
            this.dataRepository = new TableViewAnalyzerDataRepository(analyzerDataRepository);
            analyzerDataTable.setItems(FXCollections.observableArrayList(dataRepository.getTableViewAnalyzerDataList()));
        });

        saveButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter)
                    .ifPresent(file -> dataRepository.saveRepositoryToJson(file.getPath()));
        });
        uploadButton.setOnAction(actionEvent -> {
            fxWeaver.loadController(FileChooserController.class).getSingleFile(FileChooserController.jsonExtensionFilter)
                    .ifPresent(file -> {
                        try {
                            dataRepository = TableViewAnalyzerDataRepository.getRepositoryFromJson(file.getPath());
                            analyzerDataTable.getItems().clear();
                            analyzerDataTable.setItems(FXCollections.observableArrayList(dataRepository.getTableViewAnalyzerDataList()));
                        } catch (IOException e) {
                            System.err.println("No such file exception");
                        }
                    });
        });
    }
}
