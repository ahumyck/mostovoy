package com.mostovoy_company.expirement.table_experiment.analyzer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyzerData {
    private int size;
    private double probability;

    private int sumBlackCell;
    private List<Integer> emptyRows;
    private double averageBlackCellsPerColumn;

    private double whileCellsAveragePerColumn;
    private double whiteCellsAveragePerRow;
    private int maxWhiteCellsColumn;
    private int minWhiteCellsColumn;
    private int maxWhiteCellsRow;
    private int minWhiteCellsRow;

    public int getEmptyRowCounter(){
        return emptyRows.size();
    }
}
