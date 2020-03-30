package company.programminPercolation;

import company.entity.Cell;

public class PercolationRelation {
    private Cell blackCell;
    private Cell redCell;
    private double distance;

    public PercolationRelation(Cell blackCell, Cell redCell, double distance) {
        this.blackCell = blackCell;
        this.redCell = redCell;
        this.distance = distance;
    }

    public Cell getBlackCell() {
        return blackCell;
    }

    public void setBlackCell(Cell blackCell) {
        this.blackCell = blackCell;
    }

    public Cell getRedCell() {
        return redCell;
    }

    public void setRedCell(Cell redCell) {
        this.redCell = redCell;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
