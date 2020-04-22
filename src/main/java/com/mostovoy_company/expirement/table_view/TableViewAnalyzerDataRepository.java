package com.mostovoy_company.expirement.table_view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.expirement.entity.Experiment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableViewChartAnalyzerDataRepository {
    public List<TableViewAnalyzerData> tableViewAnalyzerDataList;

    public void saveExperimentToJson(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson(this, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TableViewChartAnalyzerDataRepository getTableViewFromJson(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        JsonReader jsonReader = new JsonReader(fileReader);
        TableViewChartAnalyzerDataRepository tableViewChartAnalyzerDataRepository = new Gson()
                .fromJson(jsonReader, TableViewChartAnalyzerDataRepository.class);
        fileReader.close();
        jsonReader.close();
        return tableViewChartAnalyzerDataRepository;
    }
}
