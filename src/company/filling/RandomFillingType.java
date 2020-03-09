package company.filling;

import company.entity.CellType;
import company.entity.Matrix;

import java.util.Random;

import static company.entity.Matrix.OFFSET;

public class RandomFillingType extends FillingType {
    private String name;

    private double percolationProbability;

    public RandomFillingType() {
        super("Случайная");
    }

    public void setPercolationProbability(double percolationProbability) {
        this.percolationProbability = percolationProbability;
    }

    @Override
    public void fillMatrix(Matrix matrix) {
        Random generator = new Random();
        for(int i = OFFSET; i < matrix.getSize() - OFFSET; i++){
            for(int j = OFFSET; j < matrix.getSize() - OFFSET; j++){
                if(generator.nextDouble() <= percolationProbability) matrix.getCell(i,j).setType(CellType.BLACK);
                else matrix.getCell(i,j).setType(CellType.WHITE);
            }
        }
    }
}
