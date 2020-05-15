package com.mostovoy_company.expirement.chart_experiment.programminPercolation.boundaryGenerators.predicates;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;

public interface Predicate {
    boolean check(Cell centerCell, int x, int y);
    int priority();
}
