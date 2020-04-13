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
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class Experiment {

    private Statistic statistic = new Statistic();
    private String name;
    private Matrix matrix;
    private Pair<List<Cell>, Integer> path = null;
    private List<PercolationRelation> programmings = null;
    private int neighborhood = 3;
    private Pair<Integer, Integer> darkRedAndBlackCellsRatio = null;
    private LightningBolt lightningBolt;
//    private List<Double> distances;

    public Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
        this.lightningBolt = new LightningBolt(matrix);
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        this.statistic.setClusterCount(matrix.getClusterCounter());
        this.statistic.setBlackCellCount((int)matrix.getCountOfBlackCells());
    }

    public Experiment matrix(Matrix matrix){
        this.matrix = matrix;
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        this.statistic.setClusterCount(matrix.getClusterCounter());
        this.statistic.setBlackCellCount((int)matrix.getCountOfBlackCells());
        return this;
    }

    public List<Cell> getPath() {
        if (path == null) calculatePath();
        return path.getFirst();

    }

    public int getSize() {
        return this.statistic.getSize();
    }

    public long getCountOfBlackCells() {
        return this.statistic.getBlackCellCount();
    }

    public int getClusterCounter(){
        return this.statistic.getClusterCount();
    }

    public int getRedCellsCounter() {
        return this.statistic.getRedCellCount();
    }

    public int getDistance() {
        return path != null ? path.getFirst().size() : 0;
    }

//    public List<Double> getDistances(){
//        return this.distances;
//    }

    Experiment calculateLightningBolt(){
        this.lightningBolt = new LightningBolt(this.matrix);
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.statistic.setRedCellCount(lightningBolt.getRedCellCounterForShortestPath());
        this.statistic.setPercolationWayDistance(lightningBolt.getDistanceForShortestPath());
        return this;
    }

    void calculatePath() {
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.statistic.setRedCellCount(lightningBolt.getRedCellCounterForShortestPath());
//        this.distances = lightningBolt.getDistances();

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

//    public List<Cell> generateTape(int bound){
//        return new Tape(matrix, getPath()).generateTape(bound);
//    }

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

    @Override
    public String toString() {
        return name;
    }

    Experiment clear(){
        this.matrix = null;
        this.lightningBolt = null;
        return this;
    }

    public Statistic getStatistic() {
        return statistic;
    }
}