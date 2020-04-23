package com.mostovoy_company.expirement.table_experiment.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyzerDataRepository {
    private List<AnalyzerData> analyzerDataList;
    public Stream<AnalyzerData> stream(){
        return analyzerDataList.stream();
    }
}
