package com.mostovoy_company.expirement;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.lightning.LightningBolt;
import com.mostovoy_company.lightning.Pair;
import com.mostovoy_company.programminPercolation.percolation.PercolationProgramming;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import com.mostovoy_company.programminPercolation.tape.Tape;
import com.mostovoy_company.programminPercolation.tape.TapeGenerator;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Experiment {

    private String name;
    private Matrix matrix;
    private int size;
    private Pair<List<Cell>, Integer> path = null;
    private int redCellsCounter;
    private List<PercolationRelation> programmings = null;
    private int neighborhood = 3;
    private Pair<Integer, Integer> darkRedAndBlackCellsRatio = null;
    private LightningBolt lightningBolt;
    private int clusterCounter;
    private long countOfBlackCells;
    private List<Double> distances;

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        this.lightningBolt = new LightningBolt(matrix);
        this.size = matrix.getSize() - 2 * Matrix.OFFSET;
        this.clusterCounter = matrix.getClusterCounter();
        this.countOfBlackCells = matrix.getCountOfBlackCells();
    }

    Experiment(String name) {
        this.name = name;
    }

    Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
        this.lightningBolt = new LightningBolt(matrix);
    }

    public List<Cell> getPath() {
        if (path == null) calculatePath();
        return path.getFirst();

    }

    public int getSize() {
        return size;
    }

    public long getCountOfBlackCells() {
        return countOfBlackCells;
    }

    public int getClusterCounter(){
        return this.clusterCounter;
    }

    public int getRedCellsCounter() {
        return redCellsCounter;
    }

    public int getDistance() {
        return path != null ? path.getFirst().size() : 0;
    }

    public List<Double> getDistances1(){
        return this.lightningBolt.getDistances();

    }

    public List<Double> getDistances(){
        return this.distances;

    }

    void calculatePath() {
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.redCellsCounter = lightningBolt.getRedCellCounterForShortestPath();
        this.distances = lightningBolt.getDistances();
    }

    public List<PercolationRelation> getProgrammings(String distanceCalculatorType) {
        calculateProgrammingPercolation(distanceCalculatorType);
        return programmings;
    }

    private void calculateProgrammingPercolation(String distanceCalculatorType) {
        this.programmings = new PercolationProgramming(matrix, getPath())
                .setDistanceCalculator(DistanceCalculatorTypeResolver.getDistanceCalculator(distanceCalculatorType))
                .getProgrammingPercolationList(this.neighborhood);
    }

    public List<Cell> generateTape(int bound){
        return new Tape(matrix, getPath()).generateTape(bound);
    }

    public Pair<Integer,Integer> getDarkRedAndBlackCellsFromWideTape(){
        calculateDarkRedAndBlackCellsInTape();
        return darkRedAndBlackCellsRatio;
    }

    private void calculateDarkRedAndBlackCellsInTape(){
        List<Cell> tape = new Tape(matrix, getPath()).generateWideTape(this.neighborhood);
        int blackCounter = (int)tape.stream().filter(Cell::isBlack).count();
        List<Cell> darkRedCells = this.programmings.stream()
                .map(PercolationRelation::getDarkRedCell)
                .collect(Collectors.toList());
        int darkRedCounter = (int)tape.stream().filter(Cell::isBlack).filter(darkRedCells::contains).count();
        darkRedAndBlackCellsRatio = new Pair<>(darkRedCounter, blackCounter);
    }



    public Matrix getMatrix() {
        return matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void clear(){
        this.matrix = null;
        this.lightningBolt = null;
    }
}
