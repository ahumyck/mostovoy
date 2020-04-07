package com.mostovoy_company.programminPercolation.boundaryGenerators;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.predicates.Predicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RhombusBoundaryGenerator extends BoundaryGenerator {

    public RhombusBoundaryGenerator(Matrix matrix) {
        super(matrix);
    }

    @Override
    protected void generate(int boundary, Cell centerCell,int x,int y){
        checkPredicates(centerCell, x - boundary, y);
        checkPredicates(centerCell, x + boundary, y);
        checkPredicates(centerCell, x, y + boundary);
        checkPredicates(centerCell, x, y - boundary);
        for (int i = 1; i < boundary; i++){
            checkPredicates(centerCell, x - (boundary - i), y - i);
            checkPredicates(centerCell, x - (boundary - i), y + i);
            checkPredicates(centerCell, x + (boundary - i), y - i);
            checkPredicates(centerCell, x + (boundary - i), y + i);
        }
    }
}
