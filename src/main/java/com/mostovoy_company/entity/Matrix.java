package com.mostovoy_company.entity;


import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.lightning.Pair;

import java.util.*;
import java.util.stream.Stream;

public class Matrix {
    private Cell[][] matrix;
    public static final int OFFSET = 1;
    private int clusterCounter = 0;

    private void init(int size){
        int actualSize = size + 2 * OFFSET;
        this.matrix = new Cell[actualSize][actualSize];
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                matrix[i][j] = new Cell(i,j);
            }
        }
    }

    public Matrix(FillingType type) {
        int[][] typeMatrix = type.getMatrix();
        init(typeMatrix.length);
        for (int i = 0; i < typeMatrix.length; i++) {
            for (int j = 0; j < typeMatrix.length; j++) {
                if (typeMatrix[i][j] == 1)
                    this.matrix[i + OFFSET][j + OFFSET].setType(CellType.BLACK);
                else
                    this.matrix[i + OFFSET][j + OFFSET].setType(CellType.WHITE);
            }
        }
        markClusters();
        reindexClusterMarks();
        joinClusters();
        countClusters();
    }

    public Stream<Cell> stream() {
        List<Cell> cells = new ArrayList<>();
        for (Cell[] value : this.matrix) {
            cells.addAll(Arrays.asList(value).subList(0, this.matrix.length));
        }
        return cells.stream();
    }

    public int getSize(){
        return matrix.length;
    }

    public Cell getCell(int i, int j){
        return this.matrix[i][j];
    }

    public int getClusterCounter(){
        return clusterCounter;
    }

    private int minClusterMark(Cell first,Cell second){
        return Math.min(first.getClusterMark(), second.getClusterMark());
    }

    private void reindexClusterMarks(){
        this.clusterCounter = 1;
        Map<Integer, Integer> newMarks = new HashMap<>();
        for(int i = OFFSET; i < this.matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < this.matrix.length - OFFSET; j++) {
                Cell cell = this.matrix[i][j];
                if(cell.getClusterMark() == 0) continue;
                if(!newMarks.containsKey(cell.getClusterMark())){
                    newMarks.put(cell.getClusterMark(), clusterCounter++);
                }
                cell.setClusterMark(newMarks.get(cell.getClusterMark()));
            }
        }
    }

    private void countClusters(){
        /**
         * Using HashSet to count cluster marks
         *
         * Output: set.size() - 1 because 0 will count as mark as well
         * so we need to get rid off it
         */
        Set<Integer> set = new HashSet<>();
        for(int i = OFFSET; i < this.matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < this.matrix.length - OFFSET; j++) {
                set.add(this.matrix[i][j].getClusterMark());
            }
        }
        clusterCounter = set.size() - 1;
    }

    private void markClusters(){
        /**
         * this method is used to give a percolation blocks their unique! marks
         * */
        int clusterCounter = 0;
        for(int i = OFFSET; i < this.matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < this.matrix.length - OFFSET; j++){
                Cell currentCell = this.matrix[i][j];
                if(currentCell.isBlack()){
                    Cell up = this.matrix[i - 1][j];
                    Cell left = this.matrix[i][j - 1];
                    boolean isLeftBlack = left.isBlack();
                    boolean isUpBlack = up.isBlack();
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
    }

    private void joinClusters(){
        /**
         * this method is used to join together neighbor from different clusters
         * using joinCells
         * */
        for(int i = OFFSET; i < this.matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < this.matrix.length - OFFSET; j++) {
                joinCells(i,j);
            }
        }
    }

    private void joinCells(int i,int j){
        /**
         * @param i,j - current cell coordinates
         * recursive method to join neighbor cells from different clusters
         */
        List<Pair<Integer,Integer>> path = new ArrayList<>(); //if we'll be able to find "bad" cells we need path to comeback
        path.add(new Pair<>(i,j)); //add to path current position

        while(!path.isEmpty()){
            Pair p = path.get(path.size() - 1);
            i = (int) p.getFirst();
            j = (int) p.getSecond();
            Cell currentCell = this.matrix[i][j];
            if(currentCell.hasClusterMark()) {
                Cell right = this.matrix[i][j + 1];
                Cell left = this.matrix[i][j - 1];
                Cell down = this.matrix[i + 1][j];
                Cell up = this.matrix[i - 1][j];
                //Look for "bad" cells
                if (up.hasClusterMark()) {
                    if (up.getClusterMark() > currentCell.getClusterMark()) {
                        up.setClusterMark(currentCell.getClusterMark());
                        i = i - 1;
                        path.add(new Pair<>(i,j));
                        continue;
                    }
                }
                if (left.hasClusterMark()) {
                    if (left.getClusterMark() > currentCell.getClusterMark()) {
                        left.setClusterMark(currentCell.getClusterMark());
                        j = j - 1;
                        path.add(new Pair<>(i,j));
                        continue;
                    }
                }
                if (right.hasClusterMark()) {
                    if (right.getClusterMark() > currentCell.getClusterMark()) {
                        right.setClusterMark(currentCell.getClusterMark());
                        j = j + 1;
                        path.add(new Pair<>(i,j));
                        continue;
                    }
                }
                if (down.hasClusterMark()) {
                    if (down.getClusterMark() > currentCell.getClusterMark()) {
                        down.setClusterMark(currentCell.getClusterMark());
                        i = i + 1;
                        path.add(new Pair<>(i,j));
                        continue;
                    }
                }

                // if we made it here it means we're surrounded by good cells and it's time to go back
                path.remove(path.size() - 1);
            }
            //if we made it here it means we're in bad spot and it's time to go back
            else path.remove(path.size() - 1);
        }
    }

    @Override
    public String toString() {
        int currentOffset = OFFSET;
        StringBuilder builder = new StringBuilder();
        for(int i = currentOffset; i < this.matrix.length - currentOffset; i++){
            for(int j = currentOffset; j < this.matrix.length - currentOffset; j++){
                builder.append(this.matrix[i][j].getIntType());
                builder.append('{');
                builder.append(this.matrix[i][j].getClusterMark());
                builder.append('}');
                builder.append('\t');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}