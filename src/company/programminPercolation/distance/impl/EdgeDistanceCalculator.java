package company.programminPercolation.distance.impl;

import company.entity.Cell;
import company.programminPercolation.distance.DistanceCalculator;

public class EdgeDistanceCalculator implements DistanceCalculator {
    @Override
    public double calculateDistance(Cell a, Cell b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return dx + dy;
    }
}
