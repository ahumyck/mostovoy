package com.mostovoy_company.expirement.table_view.analyzer;

import com.mostovoy_company.expirement.table_view.analyzer.data_block.AnalyzerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyzerDataRepository {
    private List<AnalyzerData> analyzerDataList;
}
