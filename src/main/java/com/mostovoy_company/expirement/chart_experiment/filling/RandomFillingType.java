package com.mostovoy_company.expirement.chart_experiment.filling;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.springframework.stereotype.Component;

@Component
public class RandomFillingType extends FillingType {
    private double percolationProbability;
    private int size;

    private static final UniformRealDistribution uniformRealDistribution = new UniformRealDistribution();;

    public void setSize(int size) {
        this.size = size;
    }

    public RandomFillingType() {
        super("Случайное засеивание");
    }

    public void setPercolationProbability(double percolationProbability) {
        this.percolationProbability = percolationProbability;
    }

    @Override
    public int[][] getMatrix() {

        int[][] matrix = new int[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (uniformRealDistribution.sample() < percolationProbability) matrix[i][j] = 1;
                else matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    @Override
    public String getName() {
        return toString() + " " + size + "x" + size + ", k=" + String.format("%.2f", percolationProbability);
    }
}
