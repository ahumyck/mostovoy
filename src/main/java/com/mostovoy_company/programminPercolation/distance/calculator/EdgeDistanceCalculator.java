package com.mostovoy_company.programminPercolation.distance.calculator;

import com.mostovoy_company.entity.Cell;

public class EdgeDistanceCalculator implements DistanceCalculator {
    @Override
    public double calculateDistance(Cell a, Cell b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return dx + dy;
    }
}
