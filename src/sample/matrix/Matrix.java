package sample.matrix;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import javafx.util.Builder;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private Cell[][] matrix;
    private int offset = 1; // Chto bi ydobno bilo obrabativat granichnie kletki

    public Matrix(int size) {
        int actualSize = size + 2*offset;
        this.matrix = new Cell[actualSize][actualSize];
        for(int i = 0 ; i < actualSize; i++){
            for(int j = 0 ; j < actualSize; j++){
                matrix[i][j] = new Cell();
            }
        }
    }

    public Matrix generateValues(double percolationProbability) throws Exception {
        if(percolationProbability < 0 || percolationProbability > 1)
            throw new Exception("percolation probability has to be in [0;1]");
        Random generator = new Random();
        for(int i = offset; i < matrix.length - offset;i++){
            for(int j = offset ; j < matrix.length - offset; j++){
                if(generator.nextDouble() >= percolationProbability) matrix[i][j].setType(CellType.BLACK);
                else matrix[i][j].setType(CellType.WHITE);
            }
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Cell[] cells : matrix) {
            for(Cell cell : cells){
                builder.append(cell.getIntType());
                builder.append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
