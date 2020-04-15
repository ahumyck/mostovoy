package com.mostovoy_company.entity;


import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.lightning.Paired;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
        return cells.stream().filter(cell -> !cell.isEmpty());
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

    public long getCountOfBlackCells(){
        return stream().filter(Cell::hasClusterMark).count();
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
         * Output: set.size() - 1 because 0 will count as cluster as well
         * so we need to get rid off it
         */

        Set<Integer> set = new HashSet<>();
        stream().filter(Cell::hasClusterMark).forEach(cell -> set.add(cell.getClusterMark()));
        clusterCounter = set.size();
    }

    private void markClusters(){
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
        for(int i = OFFSET; i < this.matrix.length - OFFSET; i++){
            for(int j = OFFSET; j < this.matrix.length - OFFSET; j++) {
                joinCells(i,j);
            }
        }
    }

    private void joinCells(int i,int j){
        List<Paired<Integer,Integer>> path = new ArrayList<>();
        path.add(new Paired<>(i,j));

        while(!path.isEmpty()){
            Paired p = path.get(path.size() - 1);
            i = (int) p.getFirst();
            j = (int) p.getSecond();
            Cell currentCell = this.matrix[i][j];
            if(currentCell.hasClusterMark()) {
                Cell right = this.matrix[i][j + 1];
                Cell left = this.matrix[i][j - 1];
                Cell down = this.matrix[i + 1][j];
                Cell up = this.matrix[i - 1][j];
                if (up.hasClusterMark()) {
                    if (up.getClusterMark() > currentCell.getClusterMark()) {
                        up.setClusterMark(currentCell.getClusterMark());
                        i = i - 1;
                        path.add(new Paired<>(i,j));
                        continue;
                    }
                }
                if (left.hasClusterMark()) {
                    if (left.getClusterMark() > currentCell.getClusterMark()) {
                        left.setClusterMark(currentCell.getClusterMark());
                        j = j - 1;
                        path.add(new Paired<>(i,j));
                        continue;
                    }
                }
                if (right.hasClusterMark()) {
                    if (right.getClusterMark() > currentCell.getClusterMark()) {
                        right.setClusterMark(currentCell.getClusterMark());
                        j = j + 1;
                        path.add(new Paired<>(i,j));
                        continue;
                    }
                }
                if (down.hasClusterMark()) {
                    if (down.getClusterMark() > currentCell.getClusterMark()) {
                        down.setClusterMark(currentCell.getClusterMark());
                        i = i + 1;
                        path.add(new Paired<>(i,j));
                        continue;
                    }
                }
                path.remove(path.size() - 1);
            }
            else path.remove(path.size() - 1);
        }
    }

    public int getMaxClusterSize(){
        Map<Integer, Integer> result = new HashMap<>();
        stream().filter(Cell::hasClusterMark).forEach(cell -> {
            result.putIfAbsent(cell.getClusterMark(), 0);
            result.computeIfPresent(cell.getClusterMark(), (key,value) -> value + 1);
        });
        return result.values().stream().mapToInt(Integer::valueOf).max().orElse(0);
    }

    public int getMinClusterSize(){
        Map<Integer, Integer> result = new HashMap<>();
        stream().filter(Cell::hasClusterMark).forEach(cell -> {
            result.putIfAbsent(cell.getClusterMark(), 0);
            result.computeIfPresent(cell.getClusterMark(), (key,value) -> value + 1);
        });
        return result.values().stream().mapToInt(Integer::valueOf).min().orElse(0);
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