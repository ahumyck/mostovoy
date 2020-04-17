package com.mostovoy_company.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyzerData {
    private int size;
    private double probability;
    private List<Integer> blackCellsPerColumn;
    private List<Integer> blackCellsPerRow;
    private double blackCellsAveragePerColumn;
    private double blackCellsAveragePerRow;
    private int emptyRows;
    private int emptyColumns;
    private int totalBlackCells;
}
