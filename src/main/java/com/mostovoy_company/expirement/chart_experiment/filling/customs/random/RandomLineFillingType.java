package com.mostovoy_company.expirement.chart_experiment.filling.customs.random;

import com.mostovoy_company.expirement.chart_experiment.filling.customs.CustomTestFillingType;

abstract public class RandomLineFillingType extends CustomTestFillingType {
    public RandomLineFillingType(String name) {
        super(name);
    }

    protected final int MATRIX_SIZE = 50;
    protected int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    @Override
    public int[][] getMatrix() {
        handler(0, MATRIX_SIZE / 4, 0, 0);
        handler(MATRIX_SIZE / 4, MATRIX_SIZE / 2, 2, 0);
        handler(MATRIX_SIZE / 2, (3 * MATRIX_SIZE) / 4, 0, 2);
        handler((3 * MATRIX_SIZE) / 4, MATRIX_SIZE, 2, 2);
        return matrix;
    }

    abstract protected void set(int first, int second, int value);

    private void handler(int start, int end, int ruleStart, int ruleEnd) {
        for (int i = start; i < end; i++) {
            int step = 5;
            if (i % step == 0) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    set(i, j, rule(ruleStart, ruleEnd, j));
                }
            }
        }
    }

    private int rule(int start, int end, int index) {
        if (index < start) return 0;
        else if (index >= MATRIX_SIZE - end) return 0;
        else return 1;
    }
}
