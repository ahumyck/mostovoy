package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.table_view.TableViewData;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerDataRepository;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerManager;
import com.mostovoy_company.expirement.table_view.stats.StatisticBlockData;
import com.mostovoy_company.expirement.table_view.stats.StatisticModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@FxmlView("analyzer.fxml")
public class AnalyzerController {

    @FXML
    public TextField matrixCountAnalyzer;
    @FXML
    public TextField matrixSizeAnalyzer;
    @FXML
    public Button applyAnalyzerExperiment;
    @FXML
    public HBox hBox;
    @FXML
    public Button saveButton;
    @FXML
    public Button uploadButton;
    @FXML
    public TableView<TableViewData> tableWhiteColumn;
    @FXML
    public TableView<TableViewData> tableWhiteRow;
    @FXML
    public TableView<TableViewData> tableBlack;

    private AnalyzerManager analyzerManager;
    private StatisticModule statisticModule;
    private FxWeaver fxWeaver;

    public AnalyzerController(AnalyzerManager analyzerManager, StatisticModule statisticModule, FxWeaver fxWeaver) {
        this.analyzerManager = analyzerManager;
        this.statisticModule = statisticModule;
        this.fxWeaver = fxWeaver;
    }

    public Node getContent() {
        return this.hBox;
    }


    @FXML
    public void initialize() {
        initializeTables();

        applyAnalyzerExperiment.setOnAction(event -> {
            int matrixSize = Integer.parseInt(this.matrixSizeAnalyzer.getText());
            int numberOfMatrices = Integer.parseInt(this.matrixCountAnalyzer.getText());
            double probability = 0.2;

            AnalyzerDataRepository analyzerDataRepository = analyzerManager.initializeAnalyzerExperiments(numberOfMatrices, matrixSize, probability);
            StatisticBlockData statisticBlockData = statisticModule.gatherStatistic(analyzerDataRepository);
            TableViewData tableBlackData = statisticBlockData.getBlackBlockData().getDataForTableViewRepresentation(matrixSize,probability);
            TableViewData dataForWhiteColumn = statisticBlockData.getWhiteBlockDataColumn().getDataForTableViewRepresentation(matrixSize,probability);
            TableViewData dataForWhiteRow = statisticBlockData.getWhiteBlockDataRow().getDataForTableViewRepresentation(matrixSize,probability);
            tableBlack.getItems().addAll(tableBlackData);
            tableWhiteRow.getItems().addAll(dataForWhiteRow);
            tableWhiteColumn.getItems().addAll(dataForWhiteColumn);
        });
//
//        saveButton.setOnAction(actionEvent -> {
//            fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter)
//                    .ifPresent(file -> dataRepository.saveRepositoryToJson(file.getPath()));
//        });
//        uploadButton.setOnAction(actionEvent -> {
//            fxWeaver.loadController(FileChooserController.class).getSingleFile(FileChooserController.jsonExtensionFilter)
//                    .ifPresent(file -> {
//                        try {
//                            dataRepository = TableViewAnalyzerDataRepository.getRepositoryFromJson(file.getPath());
//                            analyzerDataTable.getItems().clear();
//                            analyzerDataTable.setItems(FXCollections.observableArrayList(dataRepository.getTableViewAnalyzerDataList()));
//                        } catch (IOException e) {
//                            System.err.println("No such file exception");
//                        }
//                    });
//        });
    }

    void initializeTables() {
        List<String> params = generateList("firstParamAverage", "firstParamDispersion",
                "secondParamAverage", "secondParamDispersion",
                "thirdParamAverage", "thirdParamDispersion"
        );
        initializeTableView(tableWhiteColumn,
                generateList("Статистика среднего белых в столбце",
                        "Статистика минимального белых в столбце", "Статистика максимального белых в столбце"),
                params);
        initializeTableView(tableWhiteRow,
                generateList("Статистика среднего белых в строке",
                        "Статистика минимального белых в строке", "Статистика максимального белых в строке"),
                params);
        initializeTableView(tableBlack,
                generateList("Статистика количества черных клеток",
                        "Статистика пустых строк", "Статистика среднего черных в строке"),
                params);
    }

    void initializeTableView(TableView<TableViewData> tableView, List<String> columnsNames, List<String> propertyNames) {
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setColumnResizePolicy((param) -> true);

        List<TableColumn<TableViewData, String>> columns = new ArrayList<>();
        for (String columnName : columnsNames) {
            columns.add(initializeTableColumn(columnName));
        }

        int counter = 0;
        for (TableColumn<TableViewData, String> column : columns) {
            ObservableList<TableColumn<TableViewData, ?>> subColumns = column.getColumns();
            for (TableColumn<TableViewData, ?> tableViewTempTableColumn : subColumns) {
                tableViewTempTableColumn.setCellValueFactory(new PropertyValueFactory<>(propertyNames.get(counter++)));
            }
        }

        tableView.setItems(FXCollections.observableArrayList());
        tableView.getColumns().clear();
        tableView.getColumns().add(initializeBaseTableColumns("L", "size"));
        tableView.getColumns().add(initializeBaseTableColumns("концентрация", "percolation"));
        tableView.getColumns().addAll(columns);
    }


    TableColumn<TableViewData, String> initializeTableColumn(String columnName) {
        TableColumn<TableViewData, String> param = new TableColumn<>(columnName);
        TableColumn<TableViewData, String> averageFirstParam = new TableColumn<>("Среднее");
        TableColumn<TableViewData, String> dispersionFirstParam = new TableColumn<>("СКО");
        param.getColumns().addAll(averageFirstParam, dispersionFirstParam);
        return param;
    }

    TableColumn<TableViewData, String> initializeBaseTableColumns(String columnName, String propertyName) {
        TableColumn<TableViewData, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    List<String> generateList(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }
}
