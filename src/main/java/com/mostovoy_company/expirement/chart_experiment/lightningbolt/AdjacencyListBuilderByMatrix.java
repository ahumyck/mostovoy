package com.mostovoy_company.expirement.chart_experiment.lightningbolt;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost.CostRules;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.neighborhood.NeighborhoodRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdjacencyListBuilderByMatrix {
    private Map<Integer, List<Paired<Integer, Integer>>> map = new HashMap<>();
    private CostRules costRules;
    private NeighborhoodRules neighborhoodRules;

    public AdjacencyListBuilderByMatrix(CostRules costRules, NeighborhoodRules neighborhoodRules) {
        this.costRules = costRules;
        this.neighborhoodRules = neighborhoodRules;
    }

    private void add(Cell startCell, int startShiftedPosition, int i, int j, Matrix matrix) {
        int shiftedSize = matrix.getSize() - 2 * Matrix.OFFSET;
        Cell endCell = matrix.getCell(i, j);
        if (!endCell.isEmpty()) {
            int endShiftedPosition = shiftedSize * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
            int cost = costRules.setCostUsingRules(startCell, endCell, shiftedSize);
            map.get(startShiftedPosition).add(new Paired<>(endShiftedPosition, cost));
        }
    }

    public Map<Integer, List<Paired<Integer, Integer>>> build(Matrix matrix) {
        int size = matrix.getSize();
        for (int i = Matrix.OFFSET; i < size - Matrix.OFFSET; i++) {
            for (int j = Matrix.OFFSET; j < size - Matrix.OFFSET; j++) {
                Cell currentCell = matrix.getCell(i, j);
                int shiftedPosition = (size - 2 * Matrix.OFFSET) * (i - Matrix.OFFSET) + (j - Matrix.OFFSET);
                map.put(shiftedPosition, new ArrayList<>());
                neighborhoodRules.getRules(i, j)
                        .forEach(coordinates -> add(currentCell, shiftedPosition, coordinates.getFirst(), coordinates.getSecond(), matrix));
            }
        }
        return map;
    }
}
