package company.programminPercolation.boundaryGenerators.predicates;

import company.entity.Cell;

public interface Predicate {
    boolean check(Cell centerCell, int x, int y);
    int priority();
}
