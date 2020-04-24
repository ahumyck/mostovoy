package com.mostovoy_company.chart;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ChartsDataRepository {

    private Map<String, LineChartData> charts;

    @Autowired
    public ChartsDataRepository(Map<String, LineChartData> charts) {
        this.charts = charts;
    }

    public void saveChartsToJSON(String filename) {
        Map<String, Map<Integer, List<LineChartNode>>> result = new HashMap<>();
        charts.forEach((name, data) -> result.put(name, data.getUnmodifiableChartData()));
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new Gson().toJson(result, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreChartsFormJSON(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            JsonReader jsonReader = new JsonReader(fileReader);
            Map<String, Map<String, List<Map<String, Double>>>> result = new Gson().fromJson(jsonReader, HashMap.class);
            jsonReader.close();
            result.forEach(
                    (name, data) -> data.forEach(
                            (size, nodes) -> nodes.forEach(
                                    node -> charts.get(name)
                                            .add(Integer.parseInt(size), new LineChartNode(node.get("x"), node.get("y"))))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        charts.forEach((name, chart) -> chart.clear());
    }
}