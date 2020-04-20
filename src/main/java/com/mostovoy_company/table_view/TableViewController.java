package com.mostovoy_company.table_view;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class TableViewController {
    public static void initialize(TableView<TableViewAnalyzerData> analyzerDataTable){
        HBox.setHgrow(analyzerDataTable, Priority.ALWAYS);
        TableColumn<TableViewAnalyzerData, String> size = new TableColumn<>("L");
        TableColumn<TableViewAnalyzerData, String> probability = new TableColumn<>("Концентрация");
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

        analyzerDataTable.setItems(FXCollections.observableArrayList());
        analyzerDataTable.getColumns().addAll(size, probability,
                averageWhiteCellsPerColumn, minWhiteCellsPerColumn, maxWhiteCellsPerColumn,
                averageWhiteCellsPerRow, minWhiteCellsPerRow, maxWhiteCellsPerRow,
                sumBlackCells, emptyRows, averageBlackCells);
    }
}
