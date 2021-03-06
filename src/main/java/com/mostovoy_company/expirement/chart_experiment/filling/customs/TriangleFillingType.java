package com.mostovoy_company.expirement.chart_experiment.filling.customs;

import org.springframework.stereotype.Component;

@Component
public class TriangleFillingType extends CustomTestFillingType {
    private final int MATRIX_SIZE = 50;
    private final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    private static final int[][] MASK = {
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public TriangleFillingType() {
        super("Заполнение треугольниками");
    }

    @Override
    public int[][] getMatrix() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                matrix[i][j] = MASK[i % MASK.length][j % MASK.length];
            }
        }
        return matrix;
    }
}
