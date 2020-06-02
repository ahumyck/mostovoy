package com.mostovoy_company.expirement.chart_experiment.programminPercolation.percolation;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;

import java.util.ArrayList;
import java.util.List;

public class RhombusBoundaryGenerator {

    private final Matrix matrix;
    private final int matrixSize;
    private List<Cell> list;

    public RhombusBoundaryGenerator(Matrix matrix) {
        this.matrix = matrix;
        this.matrixSize = matrix.getSize();
    }


    public void checkPredicates(int x, int y) {
        if(x >= Matrix.OFFSET && x < matrixSize - Matrix.OFFSET && y >= Matrix.OFFSET && y < matrixSize - Matrix.OFFSET)
            this.list.add(this.matrix.getCell(x, y));
    }

    public List<Cell> generateArea(int boundary, Cell centerCell) {
        list = new ArrayList<>();
        int x = centerCell.getX() + Matrix.OFFSET;
        int y = centerCell.getY() + Matrix.OFFSET;
        checkPredicates(x - boundary, y);
        checkPredicates(x + boundary, y);
        checkPredicates(x, y + boundary);
        checkPredicates(x, y - boundary);
        for (int i = 1; i < boundary; i++) {
            checkPredicates(x - (boundary - i), y - i);
            checkPredicates(x - (boundary - i), y + i);
            checkPredicates(x + (boundary - i), y - i);
            checkPredicates(x + (boundary - i), y + i);
        }
        return list;
    }
}
