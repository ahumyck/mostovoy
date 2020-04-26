package com.mostovoy_company.expirement.chart_experiment.programminPercolation.boundaryGenerators;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.boundaryGenerators.predicates.Predicate;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.boundaryGenerators.predicates.RangePredicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract public class BoundaryGenerator {
    protected Matrix matrix;
    protected List<Cell> list;
    private List<Predicate> predicates;

    BoundaryGenerator(Matrix matrix) {
        this.matrix = matrix;
        this.predicates = new ArrayList<>();
        addPredicate(new RangePredicate(this.matrix.getSize()));
    }

    public BoundaryGenerator addPredicate(Predicate predicate) {
        this.predicates.add(predicate);
        this.predicates.sort(Comparator.comparingInt(Predicate::priority).reversed());
        return this;
    }

    public BoundaryGenerator addPredicates(List<Predicate> predicates) {
        this.predicates.addAll(predicates);
        this.predicates.sort(Comparator.comparingInt(Predicate::priority).reversed());
        return this;
    }

    List<Integer> getBoundaries(int bound) {
        List<Integer> boundaries = new ArrayList<>();
        for (int i = 1; i <= bound; i++) {
            boundaries.add(i);
        }
        return boundaries;
    }

    void checkPredicates(Cell centerCell, int x, int y) {
        for (Predicate predicate : this.predicates) {
            if (!predicate.check(centerCell, x, y)) return;
        }
        this.list.add(this.matrix.getCell(x, y));
    }

    protected abstract void generate(int boundary, Cell centerCell, int x, int y);

    public List<Cell> generateFilledArea(int bound, Cell centerCell) {
        list = new ArrayList<>();
        int x = centerCell.getX() + Matrix.OFFSET;
        int y = centerCell.getY() + Matrix.OFFSET;
        List<Integer> boundaries = getBoundaries(bound);
        for (Integer boundary : boundaries) {
            generate(boundary, centerCell, x, y);
        }
        return list;
    }

    public List<Cell> generateAreaPerimeter(int bound, Cell centerCell) {
        list = new ArrayList<>();
        int x = centerCell.getX() + Matrix.OFFSET;
        int y = centerCell.getY() + Matrix.OFFSET;
        generate(bound, centerCell, x, y);
        return list;
    }
}
