package com.mostovoy_company.filling.customs;

import java.text.MessageFormat;

public class ChessFillingType extends CustomTestFillingType {

    private final int MATRIX_SIZE = 50;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public ChessFillingType(){
        super("Шахматка");
    }


    @Override
    public int[][] getMatrix() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if((i + j) % 2 == 0) matrix[i][j] = 0;
                else matrix[i][j] = 1;
            }
        }
        return matrix;
    }
}
