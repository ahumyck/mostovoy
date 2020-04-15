package com.mostovoy_company.analyzer;

import com.mostovoy_company.entity.Matrix;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AnalyzerModule {

    public AnalyzerModule() {
    }

    public AnalyzerData gatherData(Matrix matrix){
        AnalyzerData data = new AnalyzerData();
        int actualSize = matrix.getSize();

        List<Integer> blackCellsPerColumn = new ArrayList<>();
        List<Integer> blackCellsPerRow = new ArrayList<>();

        int emptyRows = 0;
        int emptyColumns = 0;
        int totalBlackCells = 0;
        for (int i = Matrix.OFFSET; i < actualSize - Matrix.OFFSET; i++) {
            int blackCellRowCounter = 0;
            int blackCellColumnCounter = 0;
            for (int j = Matrix.OFFSET; j < actualSize - Matrix.OFFSET; j++) {
                if(matrix.getCell(i,j).isBlack()){
                    blackCellRowCounter++;
                    totalBlackCells++;
                }
                if(matrix.getCell(j,i).isBlack()){
                    blackCellColumnCounter++;
                }
            }
            if(blackCellColumnCounter == 0) emptyColumns++;
            if(blackCellRowCounter == 0) emptyRows++;
            blackCellsPerRow.add(blackCellRowCounter);
            blackCellsPerColumn.add(blackCellColumnCounter);
        }

        double rowAverage = blackCellsPerRow.stream()
                .mapToDouble(v-> v)
                .average().getAsDouble();
        double columnAverage = blackCellsPerColumn.stream()
                .mapToDouble(v -> v)
                .average().getAsDouble();


        data.setSize(actualSize - 2 * Matrix.OFFSET);
        data.setBlackCellsPerColumn(blackCellsPerRow);
        data.setBlackCellsPerRow(blackCellsPerColumn);
        data.setBlackCellsAveragePerColumn(rowAverage);
        data.setBlackCellsAveragePerRow(columnAverage);
        data.setEmptyColumns(emptyRows);
        data.setEmptyRows(emptyColumns);
        data.setTotalBlackCells(totalBlackCells);
        return data;
    }



}
