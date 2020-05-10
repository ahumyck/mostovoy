package com.mostovoy_company.expirement.table_experiment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableRepository {

    public static String blackTableColumnName = "Статистика по черным клеткам";
    public static String whiteTableRowName = "Статистика белых клеток в строке";
    public static String whiteTableColumnName = "Статистика белых клеток в столбце";


    private Map<String, TableView<TableViewData>> tables = new HashMap<>();

    public void createTable(String tableName, TableView<TableViewData> tableView) {
        this.tables.put(tableName, tableView);
    }


    public void clear() {
        this.tables.forEach((name, table) -> table.getItems().clear());
    }

    public void put(String tableName, TableViewData data) {
        this.tables.get(tableName).getItems().add(data);
    }

    public void saveTablesDefault(String filename) {
        StringBuilder builder = new StringBuilder();
        tables.forEach((name, table) -> {
            builder.append(name).append('\n');
            table.getItems().forEach(item -> {
                builder.append(item.getSize()).append('\t')
                        .append(item.getPercolation()).append('\t')
                        .append(item.getFirstParamAverage()).append('\t')
                        .append(item.getFirstParamDispersion()).append('\t')
                        .append(item.getSecondParamAverage()).append('\t')
                        .append(item.getSecondParamDispersion()).append('\t')
                        .append(item.getThirdParamAverage()).append('\t')
                        .append(item.getThirdParamDispersion()).append('\t')
                        .append("\n\n\n");
            });
        });
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTablesToJSON(String filename) {
        Map<String, List<TableViewData>> result = new HashMap<>();
        this.tables.forEach((name, table) -> result.put(name, new ArrayList<>(table.getItems())));
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new Gson().toJson(result, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreTablesFormJSON(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            JsonReader jsonReader = new JsonReader(fileReader);
            Map<String, List<TableViewData>> result = new Gson().fromJson(jsonReader, new TypeToken<HashMap<String, List<TableViewData>>>() {
            }.getType());
            jsonReader.close();
            result.forEach((name, columns) -> this.tables.get(name).setItems(FXCollections.observableArrayList(columns)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
