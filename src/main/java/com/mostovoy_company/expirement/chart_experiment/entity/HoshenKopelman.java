package com.mostovoy_company.expirement.chart_experiment.entity;

import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;

import java.util.*;

import static com.mostovoy_company.expirement.chart_experiment.entity.Matrix.OFFSET;

public class HoshenKopelman {
    public HoshenKopelman() {

    }

    public void clusterization(Matrix matrix) {
        markClusters(matrix);
        reindexClusterMarks(matrix);
        joinClusters(matrix);
        matrix.setClusterCount(countClusters(matrix));
    }

    private int countClusters(Matrix matrix) {
        Set<Integer> set = new HashSet<>();
        matrix.stream().filter(Cell::hasClusterMark).forEach(cell -> set.add(cell.getClusterMark()));
        return set.size();
    }

    private void reindexClusterMarks(Matrix matrix) {
        int clusterCounter = 1;
        Map<Integer, Integer> newMarks = new HashMap<>();
        for (int i = OFFSET; i < matrix.getSize() - OFFSET; i++) {
            for (int j = OFFSET; j < matrix.getSize() - OFFSET; j++) {
                Cell cell = matrix.getCell(i, j);
                if (cell.getClusterMark() == 0) continue;
                if (!newMarks.containsKey(cell.getClusterMark())) {
                    newMarks.put(cell.getClusterMark(), clusterCounter++);
                }
                cell.setClusterMark(newMarks.get(cell.getClusterMark()));
            }
        }
    }


    private int minClusterMark(Cell first, Cell second) {
        return Math.min(first.getClusterMark(), second.getClusterMark());
    }

    private void markClusters(Matrix matrix) {
        int clusterCounter = 0;
        for (int i = OFFSET; i < matrix.getSize() - OFFSET; i++) {
            for (int j = OFFSET; j < matrix.getSize() - OFFSET; j++) {
                Cell currentCell = matrix.getCell(i, j);
                if (currentCell.isBlack()) {
                    Cell up = matrix.getCell(i - 1, j);
                    Cell left = matrix.getCell(i, j - 1);
                    boolean isLeftBlack = left.isBlack();
                    boolean isUpBlack = up.isBlack();
                    if (isUpBlack && !isLeftBlack) {
                        currentCell.setClusterMark(up.getClusterMark());
                    }
                    if (!isUpBlack && isLeftBlack) {
                        currentCell.setClusterMark(left.getClusterMark());
                    }
                    if (isUpBlack && isLeftBlack) {
                        currentCell.setClusterMark(minClusterMark(up, left));
                    }
                    if (!isLeftBlack && !isUpBlack) {
                        currentCell.setClusterMark(++clusterCounter);
                    }
                }
            }
        }
    }

    private void joinClusters(Matrix matrix) {
        for (int i = OFFSET; i < matrix.getSize() - OFFSET; i++) {
            for (int j = OFFSET; j < matrix.getSize() - OFFSET; j++) {
                joinCells(matrix, i, j);
            }
        }
    }

    private void joinCells(Matrix matrix, int i, int j) {
        List<Paired<Integer, Integer>> path = new ArrayList<>();
        path.add(new Paired<>(i, j));

        while (!path.isEmpty()) {
            Paired<Integer, Integer> p = path.get(path.size() - 1);
            i = p.getFirst();
            j = p.getSecond();
            Cell currentCell = matrix.getCell(i, j);
            if (currentCell.hasClusterMark()) {
                Cell right = matrix.getCell(i, j + 1);
                Cell left = matrix.getCell(i, j - 1);
                Cell down = matrix.getCell(i + 1, j);
                Cell up = matrix.getCell(i - 1, j);
                if (up.hasClusterMark()) {
                    if (up.getClusterMark() > currentCell.getClusterMark()) {
                        up.setClusterMark(currentCell.getClusterMark());
                        i = i - 1;
                        path.add(new Paired<>(i, j));
                        continue;
                    }
                }
                if (left.hasClusterMark()) {
                    if (left.getClusterMark() > currentCell.getClusterMark()) {
                        left.setClusterMark(currentCell.getClusterMark());
                        j = j - 1;
                        path.add(new Paired<>(i, j));
                        continue;
                    }
                }
                if (right.hasClusterMark()) {
                    if (right.getClusterMark() > currentCell.getClusterMark()) {
                        right.setClusterMark(currentCell.getClusterMark());
                        j = j + 1;
                        path.add(new Paired<>(i, j));
                        continue;
                    }
                }
                if (down.hasClusterMark()) {
                    if (down.getClusterMark() > currentCell.getClusterMark()) {
                        down.setClusterMark(currentCell.getClusterMark());
                        i = i + 1;
                        path.add(new Paired<>(i, j));
                        continue;
                    }
                }
            }
            path.remove(path.size() - 1);
        }
    }
}
