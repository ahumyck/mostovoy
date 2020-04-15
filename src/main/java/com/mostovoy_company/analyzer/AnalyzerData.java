package com.mostovoy_company.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyzerData {
    private String size;
    private String probability;
    private String blackCellsPerColumn;
    private String blackCellsPerRow;
    private String blackCellsAveragePerColumn;
    private String blackCellsAveragePerRow;
    private String emptyRows;
    private String emptyColumns;
    private String totalBlackCells;
}
