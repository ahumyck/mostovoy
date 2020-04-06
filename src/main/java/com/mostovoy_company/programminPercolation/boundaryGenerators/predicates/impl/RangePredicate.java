package com.mostovoy_company.programminPercolation.boundaryGenerators.predicates.impl;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.predicates.Predicate;

public class RangePredicate implements Predicate {
    private int matrixSize;

    public RangePredicate(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    private boolean check(int x, int y) {
        return x >= Matrix.OFFSET && x < matrixSize - Matrix.OFFSET && y >= Matrix.OFFSET && y < matrixSize - Matrix.OFFSET;
    }

    @Override
    public boolean check(Cell centerCell, int x, int y) {
        return check(x, y);
    }

    @Override
    public int priority() {
        return 10;
    }
}
