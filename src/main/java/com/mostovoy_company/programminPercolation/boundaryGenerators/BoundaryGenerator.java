package com.mostovoy_company.programminPercolation.boundaryGenerators;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.boundaryGenerators.predicates.Predicate;
import com.mostovoy_company.programminPercolation.boundaryGenerators.predicates.impl.RangePredicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BoundaryGenerator {

    private Matrix matrix;
    private List<Cell> list;
    private List<Predicate> predicates;


    public BoundaryGenerator(Matrix matrix) {
        this.matrix = matrix;
        this.predicates = new ArrayList<>();
        predicates.add(new RangePredicate(this.matrix.getSize()));
    }

    public BoundaryGenerator addPredicate(Predicate predicate){
        predicates.add(predicate);
        return this;
    }

    public BoundaryGenerator addPredicates(List<Predicate> predicates){
        this.predicates.addAll(predicates);
        return this;
    }

    private void checkPredicates(Cell centerCell,int x, int y){
        for (Predicate predicate: predicates) {
            if(!predicate.check(centerCell,x,y)) return;
        }
        this.list.add(this.matrix.getCell(x,y));
    }

    private List<Integer> getBoundaries(int bound){
        List<Integer> boundaries = new ArrayList<>();
        for (int i = 1; i <= bound; i++) {
            boundaries.add(i);
        }
        return boundaries;
    }

    public List<Cell> generate(int bound, Cell centerCell){
        predicates.sort(Comparator.comparingInt(Predicate::priority).reversed());
        list = new ArrayList<>();
        int x = centerCell.getX();
        int y = centerCell.getY();
        List<Integer> boundaries = getBoundaries(bound);
        for (Integer boundary: boundaries) {
            checkPredicates(centerCell, x - boundary + Matrix.OFFSET, y + Matrix.OFFSET);
            checkPredicates(centerCell, x + boundary + Matrix.OFFSET, y + Matrix.OFFSET);
            checkPredicates(centerCell, x + Matrix.OFFSET, y + boundary + Matrix.OFFSET);
            checkPredicates(centerCell, x + Matrix.OFFSET, y - boundary + Matrix.OFFSET);
            for (int i = 1; i < boundary; i++){
                checkPredicates(centerCell, x - (boundary - i) + Matrix.OFFSET, y - i + Matrix.OFFSET);
                checkPredicates(centerCell, x - (boundary - i) + Matrix.OFFSET, y + i + Matrix.OFFSET);
                checkPredicates(centerCell, x + (boundary - i) + Matrix.OFFSET, y - i + Matrix.OFFSET);
                checkPredicates(centerCell, x + (boundary - i) + Matrix.OFFSET, y + i + Matrix.OFFSET);
            }
        }
        return list;
    }

}
