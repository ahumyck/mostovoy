package company.programminPercolation.boundaryGenerators.predicates.impl;

import company.entity.Cell;
import company.entity.Matrix;
import company.programminPercolation.boundaryGenerators.predicates.Predicate;

import java.util.List;

public class CollisionPredicate implements Predicate {

    private List<Cell> path;
    private Matrix matrix;

    public CollisionPredicate(List<Cell> path, Matrix matrix) {
        this.matrix = matrix;
        this.path = path;
    }


    private int dx(Cell centerCell, Cell potentialCell){
        return centerCell.getX() - potentialCell.getX();
    }

    private int dy(Cell centerCell, Cell potentialCell){
        return centerCell.getY() - potentialCell.getY();
    }

    @Override
    public boolean check(Cell centerCell, int x, int y) {
        Cell potentialCell = matrix.getCell(x,y);
        int dx = dx(centerCell, potentialCell);
        int dy = dy(centerCell, potentialCell);
        if(dx == 0){
            if(Math.abs(dy) == 1) return true;
            if(dy < 0){
                for (int i = 1; i < Math.abs(dy); i++) {

                }
            }
            return true;
        }
        else if(dy == 0){
            if(Math.abs(dx) == 1) return true;
            return true;
        }
        else return true;
    }

    @Override
    public int priority() {
        return 9;
    }
}
