package com.mostovoy_company.expirement.table_view.analyzer.data_block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyzerData {
    private int size;
    private double probability;
    BlackBlockData blackBlockData;
    WhiteBlockData whiteBlockData;
}
