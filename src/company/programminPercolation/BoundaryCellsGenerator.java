package company.programminPercolation;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.Pair;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class BoundaryCellsGenerator {

    private int boundary;
    private int x,y;
    private Matrix matrix;
    private List<Cell> list;

    public BoundaryCellsGenerator(int boundary, Cell centerCell, Matrix matrix) {
        this.boundary = boundary;
        this.matrix = matrix;
        this.x = centerCell.getX();
        this.y = centerCell.getY();
    }

    private void addIfCondition(int x, int y){
        if(x >= Matrix.OFFSET && x < matrix.getSize() - Matrix.OFFSET && y >= Matrix.OFFSET && y < matrix.getSize() - Matrix.OFFSET){
            this.list.add(this.matrix.getCell(x,y));
        }
    }

    public List<Cell> generate(){
        this.list = new ArrayList<>();
        addIfCondition(x - boundary + Matrix.OFFSET, y + Matrix.OFFSET);
        addIfCondition(x + boundary + Matrix.OFFSET, y + Matrix.OFFSET);
        addIfCondition(x + Matrix.OFFSET, y + boundary + Matrix.OFFSET);
        addIfCondition(x + Matrix.OFFSET, y - boundary + Matrix.OFFSET);
        for (int i = 1; i < boundary; i++){
            addIfCondition(x - (boundary - i) + Matrix.OFFSET, y - i + Matrix.OFFSET);
            addIfCondition(x - (boundary - i) + Matrix.OFFSET, y + i + Matrix.OFFSET);
            addIfCondition(x + (boundary - i) + Matrix.OFFSET, y - i + Matrix.OFFSET);
            addIfCondition(x + (boundary - i) + Matrix.OFFSET, y + i + Matrix.OFFSET);
        }
        return list;
    }

}
