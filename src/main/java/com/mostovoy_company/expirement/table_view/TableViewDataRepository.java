package com.mostovoy_company.expirement.table_view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerDataRepository;
import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableViewDataRepository {
    public List<TableViewAnalyzerData> tableViewAnalyzerDataList;

    public TableViewDataRepository(AnalyzerDataRepository dataRepository){
        this.tableViewAnalyzerDataList = new ArrayList<>();
        for (AnalyzerData data: dataRepository.getAnalyzerDataList()) {
            this.tableViewAnalyzerDataList.add(new TableViewAnalyzerData(data));
        }
    }

    public void saveRepositoryToJson(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new GsonBuilder()
                    .create().toJson(this, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TableViewDataRepository getRepositoryFromJson(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        JsonReader jsonReader = new JsonReader(fileReader);
        TableViewDataRepository tableViewDataRepository = new Gson()
                .fromJson(jsonReader, TableViewDataRepository.class);
        fileReader.close();
        jsonReader.close();
        return tableViewDataRepository;
    }
}
