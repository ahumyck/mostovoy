package sample.matrix;

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
                if(generator.nextDouble() <= percolationProbability) matrix[i][j].setType(CellType.BLACK);
                else matrix[i][j].setType(CellType.WHITE);
            }
        }
        markClusters();
        joinClusters();
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
    private Matrix markClusters(){
        int clusterCounter = 0;
        for(int i = offset; i < matrix.length - offset;i++){
            for(int j = offset ; j < matrix.length - offset; j++){
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
    private Matrix joinClusters(){
        for(int i = offset; i < matrix.length - offset; i++){
            for(int j = offset ; j < matrix.length - offset; j++) {
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
        Cell next = matrix[i][j+1];
        Cell down = matrix[i+1][j];
        if(currentCell.hasClusterMark()) {
            if(next.hasClusterMark()) {
                if (next.getClusterMark() < currentCell.getClusterMark()) {
                    currentCell.setClusterMark(next.getClusterMark());
                    joinCells(i, j + 1);
                }
            }
            if(down.hasClusterMark()) {
                if (down.getClusterMark() < currentCell.getClusterMark()) {
                    currentCell.setClusterMark(down.getClusterMark());
                    joinCells(i + 1, j);
                }
            }
        }

    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = offset; i < matrix.length - offset;i++){
            for(int j = offset ; j < matrix.length - offset; j++){
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
