package com.mostovoy_company.filling;

import java.util.Random;

public class RandomFillingType extends FillingType {
    private double percolationProbability;
    private int size;

    public void setSize(int size) {
        this.size = size;
    }

    public RandomFillingType() {
        super("Случайная");
    }

    public void setPercolationProbability(double percolationProbability) {
        this.percolationProbability = percolationProbability;
    }

    @Override
    public int[][] getMatrix() {
        Random generator = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (generator.nextDouble() <= percolationProbability) matrix[i][j] = 1;
                else matrix[i][j] = 0;
            }
        }
        return matrix;
    }
}
