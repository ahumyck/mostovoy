package com.mostovoy_company.filling.customs;

public class MaltTestFillingType extends CustomTestFillingType {

    private static final int[][] MATRIX = {
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {0, 0, 0, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0}};

    public MaltTestFillingType(String name) {
        super(name);
    }

    public MaltTestFillingType() {
        super("Мальтийский крест");

    }

    @Override
    public int[][] getMatrix() {
        return MATRIX;
    }
}