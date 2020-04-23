package com.mostovoy_company.expirement.chart_experiment.programminPercolation.distance.calculator;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;

public class PythagoreanTheoremCalculator implements DistanceCalculator {
    @Override
    public double calculateDistance(Cell a, Cell b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
