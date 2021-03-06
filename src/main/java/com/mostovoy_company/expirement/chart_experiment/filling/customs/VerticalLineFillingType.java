package com.mostovoy_company.expirement.chart_experiment.filling.customs;

import org.springframework.stereotype.Component;

@Component
public class VerticalLineFillingType  extends CustomTestFillingType {

    private final int MATRIX_SIZE = 50;
    private final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public VerticalLineFillingType() {
        super("Вертикальные линии");
    }

    @Override
    public int[][] getMatrix() {
        int step = 2;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if(i % step == 0) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrix[j][i] = 1;
                }
            }
        }
        return matrix;
    }
}