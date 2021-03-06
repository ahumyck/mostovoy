package com.mostovoy_company.controllers;

import com.mostovoy_company.expirement.table_experiment.TableViewData;
import com.mostovoy_company.expirement.table_experiment.TableRepository;
import com.mostovoy_company.expirement.table_experiment.analyzer.AnalyzerDataRepository;
import com.mostovoy_company.expirement.table_experiment.analyzer.AnalyzerManager;
import com.mostovoy_company.expirement.table_experiment.stats.StatisticBlockData;
import com.mostovoy_company.expirement.table_experiment.stats.StatisticModule;
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
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;


@Component
@Slf4j
@FxmlView("analyzer.fxml")
public class AnalyzerController {

    @FXML
    public TextField matrixCountAnalyzer;
    @FXML
    public TextField matrixSizeAnalyzer;
    @FXML
    public Button apply;
    @FXML
    public HBox hBox;
    @FXML
    public TableView<TableViewData> whiteTableColumn;
    @FXML
    public TableView<TableViewData> whiteTableRow;
    @FXML
    public TableView<TableViewData> blackTableColumn;
    @FXML
    public TextField stepProbability;
    @FXML
    public Button saveButton;
    @FXML
    public Button uploadButton;
    @FXML
    public Button clear;
    @FXML
    public Button save;

    private final AnalyzerManager analyzerManager;
    private final StatisticModule statisticModule;
    private final TableRepository repository;
    private final FxWeaver fxWeaver;

    public AnalyzerController(AnalyzerManager analyzerManager,
                              StatisticModule statisticModule,
                              TableRepository repository,
                              FxWeaver fxWeaver) {
        this.analyzerManager = analyzerManager;
        this.statisticModule = statisticModule;
        this.repository = repository;
        this.fxWeaver = fxWeaver;
    }

    Node getContent() {
        return this.hBox;
    }


    @FXML
    public void initialize() {
        initializeTables();

        apply.setOnAction(event -> {
            int matrixSize = Integer.parseInt(this.matrixSizeAnalyzer.getText());
            int numberOfMatrices = Integer.parseInt(this.matrixCountAnalyzer.getText());
            double step = Double.parseDouble(this.stepProbability.getText());


            new Thread(() -> DoubleStream.iterate(0.00, x -> x + step)
                    .limit((long) (1.0 / step + 1))
                    .filter(x -> x >= 0)
                    .filter(x -> x < 1.01)
                    .forEach(probability -> {
                        AnalyzerDataRepository analyzerDataRepository = analyzerManager.initializeAnalyzerExperiments(numberOfMatrices, matrixSize, probability);
                        StatisticBlockData statisticBlockData = statisticModule.gatherStatistic(analyzerDataRepository);
                        repository.put(TableRepository.blackTableColumnName, statisticBlockData.getBlackBlockData().getDataForTableViewRepresentation(matrixSize, probability));
                        repository.put(TableRepository.whiteTableRowName, statisticBlockData.getWhiteBlockDataRow().getDataForTableViewRepresentation(matrixSize, probability));
                        repository.put(TableRepository.whiteTableColumnName, statisticBlockData.getWhiteBlockDataColumn().getDataForTableViewRepresentation(matrixSize, probability));
                    })).start();
        });

        clear.setOnAction(event -> repository.clear());


        save.setOnAction(actionEvent -> {
            Optional<File> optionalFileName = fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.txtExtensionFilter);
            optionalFileName.ifPresent(file -> repository.saveTablesDefault(file.getPath()));
        });
        saveButton.setOnAction(actionEvent -> {
            Optional<File> optionalFileName = fxWeaver.loadController(FileChooserController.class).getFileToSave(FileChooserController.jsonExtensionFilter);
            optionalFileName.ifPresent(file -> repository.saveTablesToJSON(file.getPath()));
        });
        uploadButton.setOnAction(actionEvent -> {
            Optional<File> optionalFile = fxWeaver.loadController(FileChooserController.class).getSingleFile(FileChooserController.jsonExtensionFilter);
            optionalFile.ifPresent(file -> {
                repository.clear();
                repository.restoreTablesFormJSON(file.getPath());
            });
        });
    }

    private void initializeTables() {
        List<String> params = generateList("firstParamAverage", "firstParamDispersion",
                "secondParamAverage", "secondParamDispersion",
                "thirdParamAverage", "thirdParamDispersion"
        );
        initializeTableView(whiteTableColumn,
                generateList("Статистика среднего белых в столбце",
                        "Статистика максимального белых в столбце", "Статистика минимального белых в столбце"),
                params);
        initializeTableView(whiteTableRow,
                generateList("Статистика среднего белых в строке",
                        "Статистика максимального белых в строке", "Статистика минимального белых в строке"),
                params);
        initializeTableView(blackTableColumn,
                generateList("Статистика суммы черных клеток по столбцам",
                        "Статистика пустых строк", "Статистика среднего черных в строке"),
                params);
        repository.createTable(TableRepository.blackTableColumnName, blackTableColumn);
        repository.createTable(TableRepository.whiteTableRowName, whiteTableRow);
        repository.createTable(TableRepository.whiteTableColumnName, whiteTableColumn);
    }

    private void initializeTableView(TableView<TableViewData> tableView, List<String> columnsNames, List<String> propertyNames) {
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        tableView.getColumns().clear();
        tableView.getColumns().add(initializeBaseTableColumns("L", "size"));
        tableView.getColumns().add(initializeBaseTableColumns("концентрация", "percolation"));
        tableView.getColumns().addAll(initializeColumns(columnsNames, propertyNames));
    }

    private List<TableColumn<TableViewData, String>> initializeColumns(List<String> columnsNames, List<String> propertyNames){
        List<TableColumn<TableViewData, String>> columns = new ArrayList<>();
        for (String columnName : columnsNames) {
            columns.add(initializeTableColumn(columnName));
        }
        return initializePropertyValueFactory(columns, propertyNames);
    }


    private TableColumn<TableViewData, String> initializeTableColumn(String columnName) {
        TableColumn<TableViewData, String> param = new TableColumn<>(columnName);
        TableColumn<TableViewData, String> averageFirstParam = new TableColumn<>("Среднее");
        TableColumn<TableViewData, String> dispersionFirstParam = new TableColumn<>("СКО");
        param.getColumns().add(averageFirstParam);
        param.getColumns().add(dispersionFirstParam);
        return param;
    }

    private List<TableColumn<TableViewData, String>> initializePropertyValueFactory(List<TableColumn<TableViewData, String>> columns, List<String> propertyNames){
        int counter = 0;
        for (TableColumn<TableViewData, String> column : columns) {
            ObservableList<TableColumn<TableViewData, ?>> subColumns = column.getColumns();
            for (TableColumn<TableViewData, ?> tableViewTempTableColumn : subColumns) {
                tableViewTempTableColumn.setCellValueFactory(new PropertyValueFactory<>(propertyNames.get(counter++)));
            }
        }
        return columns;
    }


    private TableColumn<TableViewData, String> initializeBaseTableColumns(String columnName, String propertyName) {
        TableColumn<TableViewData, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private List<String> generateList(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }
}
