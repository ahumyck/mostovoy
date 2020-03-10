package company.filling;

import company.entity.CellType;
import company.entity.Matrix;

import java.util.Random;

import static company.entity.Matrix.OFFSET;

public class RandomFillingTypeV2 extends FillingTypeV2{
    private double percolationProbability;
    private int size;
    public RandomFillingTypeV2() {
        super("Случайная");
    }

    public void setPercolationProbability(double percolationProbability) {
        this.percolationProbability = percolationProbability;
    }

    @Override
    public int[][] getMatrix() {
        Random generator = new Random();
        int[][] matrix = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(generator.nextDouble() <= percolationProbability) matrix[i][j] = 0;
                else matrix[i][j] = 0;
            }
        }
        return matrix;
    }
}
