package com.mostovoy_company.programminPercolation.distance.impl;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculator;

public class PythagoreanTheoremCalculator implements DistanceCalculator {
    @Override
    public double calculateDistance(Cell a, Cell b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
