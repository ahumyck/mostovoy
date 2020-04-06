package com.mostovoy_company.programminPercolation.boundaryGenerators.predicates;

import com.mostovoy_company.entity.Cell;

public interface Predicate {
    boolean check(Cell centerCell, int x, int y);
    int priority();
}
