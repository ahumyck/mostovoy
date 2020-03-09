package sample.matrix;

import java.util.Random;

public class Matrix {
    private Cell[][] matrix;
    private final int OFFSET = 1; // Chto bi ydobno bilo obrabativat granichnie kletki

    public Matrix(int size) {
        int actualSize = size + 2 * OFFSET;
        this.matrix = new Cell[actualSize][actualSize];
        for(int i = 0 ; i < actualSize; i++){
            for(int j = 0 ; j < actualSize; j++){
                matrix[i][j] = new Cell();
            }
        }
    }

    public int getSize(){
        return matrix.length;
    }

    public Cell getCell(int i,int j){
        return matrix[i][j];
    }

    public Matrix generateValues(double percolationProbability) throws Exception {
        if(percolationProbability < 0 || percolationProbability > 1)
            throw new Exception("percolation probability has to be in [0;1]");
        Random generator = new Random();
        int clusterCounter = 0;
        for(int i = OFFSET; i < matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < matrix.length - OFFSET; j++){
                Cell currentCell = matrix[i][j];
                if(generator.nextDouble() <= percolationProbability) currentCell.setType(CellType.BLACK);
                else currentCell.setType(CellType.WHITE);

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

    private boolean isBlack(Cell cell){
        return cell.getType().equals(CellType.BLACK);
    }

    private int minClusterMark(Cell first,Cell second){
        return Math.min(first.getClusterMark(), second.getClusterMark());
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
