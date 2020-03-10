package company.entity;


import company.filling.FillingType;
import company.filling.FillingTypeV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Matrix {
    private Cell[][] matrix;
    public static final int OFFSET = 1; // Chto bi ydobno bilo obrabativat granichnie kletki

    public Matrix(int size) {
        int actualSize = size + 2 * OFFSET;
        this.matrix = new Cell[actualSize][actualSize];
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                matrix[i][j] = new Cell(i,j);
            }
        }
    }

    public Matrix(FillingTypeV2 type){
        int[][] matrix = type.getMatrix();
        int actualSize = matrix.length + 2 * OFFSET;
        this.matrix = new Cell[actualSize][actualSize];
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                this.matrix[i][j] = new Cell(i,j);
                if(i < OFFSET || i > actualSize - OFFSET) continue;
                if(j < OFFSET || j > actualSize - OFFSET) continue;
                if(matrix[i][j] == 1)
                    this.matrix[i][i].setType(CellType.BLACK);
                else
                    this.matrix[i][j].setType(CellType.WHITE);
            }
        }
    }


    public Stream<Cell> stream() {
        List<Cell> cells = new ArrayList<>();
        for (Cell[] value : matrix) {
            cells.addAll(Arrays.asList(value).subList(0, matrix.length));
        }
        return cells.stream();
    }

    public int getSize(){
        return matrix.length;
    }

    public Cell getCell(int i, int j){
        return matrix[i][j];
    }

    public Matrix generateValues(FillingType fillingType) /*throws Exception*/ {
        /*if(percolationProbability < 0 || percolationProbability > 1)
            throw new Exception("percolation probability has to be in [0;1]");*/

        fillingType.fillMatrix(this);
        return this;
    }

    private boolean isBlack(Cell cell){
        return cell.getType().equals(CellType.BLACK);
    }

    private int minClusterMark(Cell first,Cell second){
        return Math.min(first.getClusterMark(), second.getClusterMark());
    }

    /**
     * this method is used to give a percolation blocks their marks
     * */
    public Matrix markClusters(){
        int clusterCounter = 0;
        for(int i = OFFSET; i < matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < matrix.length - OFFSET; j++){
                Cell currentCell = matrix[i][j];
                if(isBlack(currentCell)){
                    Cell up = matrix[i - 1][j];
                    Cell left = matrix[i][j - 1];
                    boolean isLeftBlack = isBlack(left);
                    boolean isUpBlack = isBlack(up);
                    if(isUpBlack && !isLeftBlack){
                        currentCell.setClusterMark(up.getClusterMark());
                    }
                    if(!isUpBlack && isLeftBlack){
                        currentCell.setClusterMark(left.getClusterMark());
                    }
                    if(isUpBlack && isLeftBlack){
                        currentCell.setClusterMark(minClusterMark(up,left));
                    }
                    if(!isLeftBlack && !isUpBlack){
                        currentCell.setClusterMark(++clusterCounter);
                    }
                }
            }
        }
        return this;
    }

    /**
     * this method is used to join clusters together
     * this method uses joinCells to join neighbor cells from different clusters together
     * */
    public Matrix joinClusters(){
        for(int i = OFFSET; i < matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < matrix.length - OFFSET; j++) {
                joinCells(i,j);
            }
        }
        return this;
    }

    public void lightningBolt(){
        return;
    }


    private int dijksta(int st){
        /**
         * start point: matrix[offset][offset + start]
         * end points: matrix[offset][offset: matrix.length - offset]
         * */
        int n = matrix.length - 2 * OFFSET;

        return 0;
    }

    /**
     * @param i,j - current cell coordinates
     * recursive method to join neighbor cells from different clusters
     */
    private void joinCells(int i,int j){
        Cell currentCell = matrix[i][j];
        if(currentCell.hasClusterMark()) {
            Cell right = matrix[i][j+1];
            Cell left = matrix[i][j-1];
            Cell down = matrix[i+1][j];
            Cell up = matrix[i-1][j];
            if(up.hasClusterMark()) {
                if (up.getClusterMark() > currentCell.getClusterMark()) {
                    up.setClusterMark(currentCell.getClusterMark());
                    joinCells(i-1, j);
                }
            }
            if(left.hasClusterMark()) {
                if (left.getClusterMark() > currentCell.getClusterMark()) {
                    left.setClusterMark(currentCell.getClusterMark());
                    joinCells(i, j - 1);
                }
            }
            if(right.hasClusterMark()) {
                if (right.getClusterMark() > currentCell.getClusterMark()) {
                    right.setClusterMark(currentCell.getClusterMark());
                    joinCells(i, j + 1);
                }
            }
            if(down.hasClusterMark()) {
                if (down.getClusterMark() > currentCell.getClusterMark()) {
                    down.setClusterMark(currentCell.getClusterMark());
                    joinCells(i + 1, j);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = OFFSET; i < matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < matrix.length - OFFSET; j++){
                builder.append(matrix[i][j].getIntType());
                builder.append('{');
                builder.append(matrix[i][j].getClusterMark());
                builder.append('}');
                builder.append('\t');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}