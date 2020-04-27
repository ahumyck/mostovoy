package com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency.neighborhood.NeighborhoodRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyListBuilderByMatrix {
    private Map<Integer, List<Paired<Integer, Integer>>> map = new HashMap<>();

    private int setCostUsingRules(Cell startCell, Cell endCell, int shiftedSize) {
        int cheapCost = 1;
        int expensiveCost = shiftedSize * shiftedSize + 1;
        int cost = 0;

        if (startCell.isBlack() && endCell.isBlack())  //black -> black - cheap
            cost = cheapCost;
        if (startCell.isBlack() && endCell.isWhite()) // black -> white - expensive
            cost = expensiveCost;
        if (startCell.isWhite() && endCell.isBlack()) // white -> black - half expensive
            cost = cheapCost;
        if (startCell.isWhite() && endCell.isWhite()) // white -> white - twice expensive
            cost = expensiveCost;

        return cost;
    }

    private void add(Cell startCell, int startShiftedPosition, int i, int j, Matrix matrix) {
        int shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        Cell endCell = matrix.getCell(i, j);
        if (!endCell.isEmpty()) {
            int endShiftedPosition = shiftedSize * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
            int cost = setCostUsingRules(startCell, endCell, shiftedSize);
            map.get(startShiftedPosition).add(new Paired<>(endShiftedPosition, cost));
        }
    }

    public Map<Integer, List<Paired<Integer, Integer>>> build(Matrix matrix, NeighborhoodRules rules) {
        int size = matrix.getSize();
        for (int i = Matrix.OFFSET; i < size - Matrix.OFFSET; i++) {
            for (int j = Matrix.OFFSET; j < size - Matrix.OFFSET; j++) {
                Cell currentCell = matrix.getCell(i, j);
                int shiftedPosition = (size - 2 * Matrix.OFFSET) * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
                map.put(shiftedPosition, new ArrayList<>());
                rules.getRules(i, j)
                        .forEach(coordinates -> add(currentCell, shiftedPosition, coordinates.getFirst(), coordinates.getSecond(), matrix));
            }
        }
//        map.forEach((pos, n) -> System.out.println(pos + " = " + n));
        return map;
    }
}
