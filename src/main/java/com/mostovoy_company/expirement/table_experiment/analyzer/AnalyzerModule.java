package com.mostovoy_company.expirement.table_experiment.analyzer;

import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AnalyzerModule {

    public AnalyzerModule() {
    }

    public AnalyzerData gatherData(Matrix matrix, double probability) {
        int actualSize = matrix.getSize();

        List<Integer> blackCellsPerColumn = new ArrayList<>();

        List<Integer> whiteCellsPerColumn = new ArrayList<>();
        List<Integer> whiteCellsPerRow = new ArrayList<>();

        List<Integer> emptyRows = new ArrayList<>();
        int totalBlackCells = 0;
        for (int i = Matrix.OFFSET; i < actualSize - Matrix.OFFSET; i++) {
            int blackCellRowCounter = 0;
            int whiteCellRowCounter = 0;
            int whiteCellColumnCounter = 0;
            for (int j = Matrix.OFFSET; j < actualSize - Matrix.OFFSET; j++) {
                if (matrix.getCell(j, i).isBlack()) {
                    blackCellRowCounter++;
                    totalBlackCells++;
                }
                if (matrix.getCell(i, j).isWhite()) {
                    whiteCellRowCounter++;
                }

                if (matrix.getCell(j, i).isWhite()) {
                    whiteCellColumnCounter++;
                }
            }
            if (blackCellRowCounter == 0) emptyRows.add(i - Matrix.OFFSET);
            whiteCellsPerColumn.add(whiteCellColumnCounter);
            whiteCellsPerRow.add(whiteCellRowCounter);
            blackCellsPerColumn.add(blackCellRowCounter);
        }

        return new AnalyzerData(
                actualSize - 2 * Matrix.OFFSET,
                probability,
                totalBlackCells,
                emptyRows,
                blackCellsPerColumn.stream().mapToDouble(i -> i)
                        .average().getAsDouble(),
                whiteCellsPerColumn.stream().mapToDouble(i -> i)
                        .average().getAsDouble(),
                whiteCellsPerRow.stream().mapToDouble(i -> i)
                        .average().getAsDouble(),
                whiteCellsPerColumn.stream().mapToInt(i -> i)
                        .max().getAsInt(),
                whiteCellsPerColumn.stream().mapToInt(i -> i)
                        .min().getAsInt(),
                whiteCellsPerRow.stream().mapToInt(i -> i)
                        .max().getAsInt(),
                whiteCellsPerRow.stream().mapToInt(i -> i)
                        .min().getAsInt());
    }
}
