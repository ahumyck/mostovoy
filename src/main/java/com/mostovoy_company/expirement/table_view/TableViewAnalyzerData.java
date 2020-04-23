package com.mostovoy_company.expirement.table_view;

import com.mostovoy_company.expirement.table_view.analyzer.AnalyzerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableViewAnalyzerData {
    private String size;
    private String probability;

    private String averageWhiteCellsPerColumns;
    private String minWhiteCellsPerColumn;
    private String maxWhiteCellsPerColumn;
    private String averageWhiteCellsPerRow;
    private String minWhiteCellsPerRow;
    private String maxWhiteCellsPerRow;

    private String sumBlackCell;
    private String emptyRows;
    private String averageBlackCells;


    public TableViewAnalyzerData(AnalyzerData data){
        this.size = String.valueOf(data.getSize());
        this.probability = String.format("%.2f", data.getProbability());

        this.averageWhiteCellsPerColumns = String.format("%.2f", data.getWhileCellsAveragePerColumn());
        this.minWhiteCellsPerColumn = String.valueOf(data.getMinWhiteCellsColumn());
        this.maxWhiteCellsPerColumn = String.valueOf(data.getMaxWhiteCellsColumn());
        this.averageWhiteCellsPerRow = String.format("%.2f", data.getWhiteCellsAveragePerRow());
        this.minWhiteCellsPerRow = String.valueOf(data.getMinWhiteCellsRow());
        this.maxWhiteCellsPerRow = String.valueOf(data.getMaxWhiteCellsRow());


        this.sumBlackCell = String.valueOf(data.getSumBlackCell());
        if(data.getEmptyRows().isEmpty()) this.emptyRows = " - ";
        else this.emptyRows = data.getEmptyRows().toString();
        this.averageBlackCells = String.format("%.2f", data.getAverageBlackCellsPerRow());

    }
}
