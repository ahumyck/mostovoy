package com.mostovoy_company.analyzer;

import com.mostovoy_company.entity.Matrix;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AnalyzerModule {

    public AnalyzerModule() {
    }

    public AnalyzerData gatherData(Matrix matrix, double probability){
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


        return new AnalyzerData(String.valueOf(actualSize - 2 * Matrix.OFFSET),
                String.format("%.2f", probability),
                blackCellsPerRow.toString(),
                blackCellsPerColumn.toString(),
                String.format("%.2f", rowAverage),
                String.format("%.2f", columnAverage),
                String.valueOf(emptyRows),
                String.valueOf(emptyColumns),
                String.valueOf(totalBlackCells));
    }



}
