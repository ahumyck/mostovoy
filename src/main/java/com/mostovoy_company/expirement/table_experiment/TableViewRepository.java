package com.mostovoy_company.expirement.table_experiment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import javafx.collections.FXCollections;
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
public class TableViewRepository {

    public static String blackTableRowName = "Статистика черных клеток в строке";
    public static String whiteTableRowName = "Статистика белых клеток в строке";
    public static String whiteTableColumnName = "Статистика белых клеток в столбце";


    private Map<String, Table<TableViewData>> tables = new HashMap<>();

    public void createTable(String tableName, TableView<TableViewData> tableView) {
        this.tables.put(tableName, new Table<>(tableName, tableView));
    }


    public void clear() {
        this.tables.forEach((name, table) -> table.getTableView().getItems().clear());
    }

    public void put(String tableName, TableViewData data){
        this.tables.get(tableName).getTableView().getItems().add(data);
    }

    public void saveTablesToJSON(String filename) {
        Map<String, List<TableViewData>> result = new HashMap<>();
        this.tables.forEach((name, table) -> result.put(name, new ArrayList<>(table.getTableView().getItems())));
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new Gson().toJson(result, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreTablesFormJSON(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            JsonReader jsonReader = new JsonReader(fileReader);
            Map<String, List<TableViewData>> result = new Gson().fromJson(jsonReader, new TypeToken<HashMap<String, List<TableViewData>>>() {}.getType());
            jsonReader.close();
            result.forEach((name, columns) -> this.tables.get(name).getTableView().setItems(FXCollections.observableArrayList(columns)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
