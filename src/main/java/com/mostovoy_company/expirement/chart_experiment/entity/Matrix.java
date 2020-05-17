package com.mostovoy_company.expirement.chart_experiment.entity;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.expirement.chart_experiment.filling.FillingType;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Matrix {
    @Expose
    private Cell[][] matrix;
    @Expose
    private int clusterCounter = 0;
    public static final int OFFSET = 1;

    private void init(int size) {
        int actualSize = size + 2 * OFFSET;
        this.matrix = new Cell[actualSize][actualSize];
        for (int i = 0; i < actualSize; i++) {
            for (int j = 0; j < actualSize; j++) {
                matrix[i][j] = new Cell(i, j);
            }
        }
    }

    public static Matrix fromJSON(String jsonName) throws IOException {
        FileReader fileReader = new FileReader(jsonName);
        JsonReader jsonReader = new JsonReader(fileReader);
        Matrix matrix = new Gson().fromJson(jsonReader, Matrix.class);
        fileReader.close();
        jsonReader.close();
        return matrix;
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
    }


    public Stream<Cell> stream() {
        List<Cell> cells = new ArrayList<>();
        for (Cell[] value : this.matrix) {
            cells.addAll(Arrays.asList(value).subList(0, this.matrix.length));
        }
        return cells.stream()
                    .filter(cell -> !cell.isEmpty());
    }

    public int getSize() {
        return matrix.length;
    }

    public Cell getCell(int i, int j) {
        return this.matrix[i][j];
    }

    public void setCell(int i, int j, Cell cell) {
        this.matrix[i][j] = cell;
    }

    public int getClusterCount() {
        return clusterCounter;
    }

    public void setClusterCount(int clusterCount){
        this.clusterCounter = clusterCount;
    }

    public long getCountOfBlackCells() {
        return stream().filter(Cell::isBlack).count();
    }

    public int getMaxClusterSize() {
        Map<Integer, Integer> result = new HashMap<>();
        stream().filter(Cell::hasClusterMark).forEach(cell -> {
            result.putIfAbsent(cell.getClusterMark(), 0);
            result.computeIfPresent(cell.getClusterMark(), (key, value) -> value + 1);
        });
        return result.values().stream().mapToInt(Integer::valueOf).max().orElse(0);
    }

    public int getMinClusterSize() {
        Map<Integer, Integer> result = new HashMap<>();
        stream().filter(Cell::hasClusterMark).forEach(cell -> {
            result.putIfAbsent(cell.getClusterMark(), 0);
            result.computeIfPresent(cell.getClusterMark(), (key, value) -> value + 1);
        });
        return result.values().stream().mapToInt(Integer::valueOf).min().orElse(0);
    }

    @Override
    public String toString() {
        int currentOffset = OFFSET;
        StringBuilder builder = new StringBuilder();
        for (int i = currentOffset; i < this.matrix.length - currentOffset; i++) {
            for (int j = currentOffset; j < this.matrix.length - currentOffset; j++) {
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

    public void toJSON(String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        new Gson().toJson(this, fileWriter);
        fileWriter.close();
    }
}