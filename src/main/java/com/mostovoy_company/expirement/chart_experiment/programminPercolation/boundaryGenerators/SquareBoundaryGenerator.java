package com.mostovoy_company.expirement.chart_experiment.programminPercolation.boundaryGenerators;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;

import java.util.List;

public class SquareBoundaryGenerator extends BoundaryGenerator {
    public SquareBoundaryGenerator(Matrix matrix) {
        super(matrix);
    }

    private void buildRightWall(Cell centerCell, int x, int y, int boundary) {
        int x0 = x + boundary;
        int y0 = y - boundary + 1;
        while (y0 < y + boundary) {
            checkPredicates(centerCell, x0, y0);
            y0++;
        }
    }

    private void buildLeftWall(Cell centerCell, int x, int y, int boundary) {
        int x0 = x - boundary;
        int y0 = y - boundary + 1;
        while (y0 < y + boundary) {
            checkPredicates(centerCell, x0, y0);
            y0++;
        }
    }

    private void buildTopWall(Cell centerCell, int x, int y, int boundary) {
        int x0 = x - boundary + 1;
        int y0 = y - boundary;
        while (x0 < x + boundary) {
            checkPredicates(centerCell, x0, y0);
            x0++;
        }
    }

    private void buildBotWall(Cell centerCell, int x, int y, int boundary) {
        int x0 = x - boundary + 1;
        int y0 = y + boundary;
        while (x0 < x + boundary) {
            checkPredicates(centerCell, x0, y0);
            x0++;
        }
    }

    @Override
    protected void generate(int boundary, Cell centerCell, int x, int y) {
        checkPredicates(centerCell, x - boundary, y - boundary);
        checkPredicates(centerCell, x + boundary, y - boundary);
        checkPredicates(centerCell, x - boundary, y + boundary);
        checkPredicates(centerCell, x + boundary, y + boundary);
        buildRightWall(centerCell, x, y, boundary);
        buildLeftWall(centerCell, x, y, boundary);
        buildTopWall(centerCell, x, y, boundary);
        buildBotWall(centerCell, x, y, boundary);
    }

    @Override
    public List<Cell> generateFilledArea(int bound, Cell centerCell) {
        return generateAreaPerimeter(bound, centerCell);
    }
}
