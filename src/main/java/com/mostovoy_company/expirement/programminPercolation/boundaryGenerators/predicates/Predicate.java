package com.mostovoy_company.expirement.programminPercolation.boundaryGenerators.predicates;

import com.mostovoy_company.expirement.entity.Cell;

public interface Predicate {
    boolean check(Cell centerCell, int x, int y);
    int priority();
}
