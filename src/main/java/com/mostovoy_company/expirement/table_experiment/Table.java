package com.mostovoy_company.expirement.table_experiment;

import javafx.scene.control.TableView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Table<T> {
    private String tableName;
    private TableView<T> tableView;
}
