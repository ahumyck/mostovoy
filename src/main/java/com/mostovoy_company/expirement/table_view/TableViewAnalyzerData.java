package com.mostovoy_company.expirement.table_view;

import com.mostovoy_company.expirement.table_view.analyzer.data_block.AnalyzerData;
import com.mostovoy_company.expirement.table_view.analyzer.data_block.BlackBlockData;
import com.mostovoy_company.expirement.table_view.analyzer.data_block.WhiteBlockData;
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

        WhiteBlockData whiteBlockData = data.getWhiteBlockData();
        this.averageWhiteCellsPerColumns = String.format("%.2f", whiteBlockData.getWhileCellsAveragePerColumn());
        this.minWhiteCellsPerColumn = String.valueOf(whiteBlockData.getMinWhiteCellsColumn());
        this.maxWhiteCellsPerColumn = String.valueOf(whiteBlockData.getMaxWhiteCellsColumn());
        this.averageWhiteCellsPerRow = String.format("%.2f", whiteBlockData.getWhiteCellsAveragePerRow());
        this.minWhiteCellsPerRow = String.valueOf(whiteBlockData.getMinWhiteCellsRow());
        this.maxWhiteCellsPerRow = String.valueOf(whiteBlockData.getMaxWhiteCellsRow());


        BlackBlockData blackBlockData = data.getBlackBlockData();
        this.sumBlackCell = String.valueOf(blackBlockData.getSumBlackCell());
        if(blackBlockData.getEmptyRows().isEmpty()) this.emptyRows = " - ";
        else this.emptyRows = blackBlockData.getEmptyRows().toString();
        this.averageBlackCells = String.format("%.2f", blackBlockData.getAverageBlackCells());

    }
}
