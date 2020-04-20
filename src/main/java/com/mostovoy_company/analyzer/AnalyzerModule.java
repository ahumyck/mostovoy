package com.mostovoy_company.analyzer;

import com.mostovoy_company.analyzer.data_block.AnalyzerData;
import com.mostovoy_company.analyzer.data_block.BlackBlockData;
import com.mostovoy_company.analyzer.data_block.WhiteBlockData;
import com.mostovoy_company.entity.Matrix;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;


@Component
public class AnalyzerModule {

    public AnalyzerModule() {
    }

    public AnalyzerData gatherData(Matrix matrix, double probability){
        int actualSize = matrix.getSize();

        List<Integer> blackCellsPerRow = new ArrayList<>();

        List<Integer> whiteCellsPerColumn = new ArrayList<>();
        List<Integer> whiteCellsPerRow = new ArrayList<>();

        List<Integer> emptyRows = new ArrayList<>();
        int totalBlackCells = 0;
        for (int i = Matrix.OFFSET; i < actualSize - Matrix.OFFSET; i++) {
            int blackCellRowCounter = 0;
            int whiteCellRowCounter = 0;
            int whiteCellColumnCounter = 0;
            for (int j = Matrix.OFFSET; j < actualSize - Matrix.OFFSET; j++) {
                if(matrix.getCell(i,j).isBlack()){
                    blackCellRowCounter++;
                    totalBlackCells++;
                }
                else if(matrix.getCell(i,j).isWhite()){
                    whiteCellRowCounter++;
                }

                if(matrix.getCell(j,i).isWhite()){
                    whiteCellColumnCounter++;
                }
            }
            if(blackCellRowCounter == 0) emptyRows.add(i - Matrix.OFFSET);
            whiteCellsPerColumn.add(whiteCellColumnCounter);
            whiteCellsPerRow.add(whiteCellRowCounter);
            blackCellsPerRow.add(blackCellRowCounter);
        }

        return new AnalyzerData(actualSize - 2 * Matrix.OFFSET, probability,
                gatherDataToBlackBlock(blackCellsPerRow, emptyRows, totalBlackCells),
                gatherDataToWhiteBlock(whiteCellsPerColumn, whiteCellsPerRow));
    }


    private WhiteBlockData gatherDataToWhiteBlock(List<Integer> whiteCellsPerColumn,  List<Integer> whiteCellsPerRow){
        int maxOfColumns = whiteCellsPerColumn.stream().mapToInt(i -> i)
                .max().getAsInt();
        int maxOfRows = whiteCellsPerRow.stream().mapToInt(i->i)
                .max().getAsInt();

        int minOfColumns = whiteCellsPerColumn.stream().mapToInt(i -> i)
                .max().getAsInt();
        int minOfRows = whiteCellsPerRow.stream().mapToInt(i->i)
                .max().getAsInt();


        double averageOfColumns = whiteCellsPerColumn.stream().mapToDouble(i -> i)
                .max().getAsDouble();
        double averageOfRows = whiteCellsPerRow.stream().mapToDouble(i->i)
                .max().getAsDouble();

        return new WhiteBlockData(averageOfColumns, averageOfRows, maxOfColumns, minOfColumns, maxOfRows, minOfRows);

    }

    private BlackBlockData gatherDataToBlackBlock(List<Integer> blackCellsPerRow, List<Integer> emptyRows, int totalBlackCells){
        double averageBlackCells = blackCellsPerRow.stream()
                .mapToDouble(v -> v)
                .average().getAsDouble();

        return new BlackBlockData(totalBlackCells, emptyRows, averageBlackCells);
    }


}
