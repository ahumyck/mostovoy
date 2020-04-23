package com.mostovoy_company.expirement.chart_experiment.filling.customs;

import org.springframework.stereotype.Component;

@Component
public class HorizontalLineFillingType extends CustomTestFillingType {

    private final int MATRIX_SIZE = 50;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public HorizontalLineFillingType() {
        super("Горизонтальные линии");
    }

    @Override
    public int[][] getMatrix() {
        int step = 2;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if(i % step == 0) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrix[i][j] = 1;
                }
            }
        }
        return matrix;
    }
}
