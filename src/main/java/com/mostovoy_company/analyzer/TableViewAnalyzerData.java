package com.mostovoy_company.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableViewAnalyzerData {
    private String size;
    private String probability;
    private String blackCellsPerColumn;
    private String blackCellsPerRow;
    private String blackCellsAveragePerColumn;
    private String blackCellsAveragePerRow;
    private String emptyRows;
    private String emptyColumns;
    private String totalBlackCells;

    public TableViewAnalyzerData(AnalyzerData data){
        this.size = String.valueOf(data.getSize());
        this.probability = String.format("%.2f", data.getProbability());
        this.blackCellsPerRow = data.getBlackCellsPerRow().toString();
        this.blackCellsPerColumn = data.getBlackCellsPerColumn().toString();
        this.blackCellsAveragePerRow = String.format("%.2f", data.getBlackCellsAveragePerRow());
        this.blackCellsAveragePerColumn = String.format("%.2f", data.getBlackCellsAveragePerColumn());
        this.emptyRows = String.valueOf(data.getEmptyRows());
        this.emptyColumns = String.valueOf(data.getEmptyColumns());
        this.totalBlackCells = String.valueOf(data.getTotalBlackCells());
    }
}
