package com.mostovoy_company.expirement;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.lightning.LightningBolt;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.programminPercolation.percolation.PercolationProgramming;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import com.mostovoy_company.lightning.Paired;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class Experiment {

    private Statistic statistic = new Statistic();
    private String name;
    private Matrix matrix;
    private Paired<List<Cell>, Integer> path = null;
//    private List<PercolationRelation> programmings = null;
    private int neighborhood;
    private Paired<Integer, Integer> darkRedAndBlackCellsRatio = null;
    private LightningBolt lightningBolt;
//    private List<Double> distances;

    public Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
        this.lightningBolt = new LightningBolt(matrix);
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        this.statistic.setClusterCount(matrix.getClusterCounter());
        this.statistic.setBlackCellCount((int)matrix.getCountOfBlackCells());
        this.neighborhood = 2*(matrix.getSize() - 2 * Matrix.OFFSET);
    }

    public Experiment matrix(Matrix matrix){
        this.matrix = matrix;
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        this.statistic.setClusterCount(matrix.getClusterCounter());
        this.statistic.setBlackCellCount((int)matrix.getCountOfBlackCells());
//        this.statistic.setMaxClusterSize(matrix.getMaxClusterSize());
//        this.statistic.setMinClusterSize(matrix.getMinClusterSize());
        this.neighborhood = 2*(matrix.getSize() - 2 * Matrix.OFFSET);
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

    public Experiment calculateLightningBolt(){
        this.lightningBolt = new LightningBolt(this.matrix);
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.statistic.setRedCellCount(getRedCell());
        this.statistic.setPercolationizated(getRedCell() == 0);
        this.statistic.setPercolationWayWidth(percolationWayWidth());
        this.statistic.setPercolationWayDistance(lightningBolt.getDistanceForShortestPath());
        return this;
    }

    private int percolationWayWidth(){
        return this.path.getFirst().stream().mapToInt(Cell::getY).max().getAsInt() - this.path.getFirst().stream().mapToInt(Cell::getY).min().getAsInt();
    }

    public Experiment putPercolationProgrammingInStats(){
        String[] calculators = {DistanceCalculatorTypeResolver.PYTHAGORAS, DistanceCalculatorTypeResolver.DISCRETE};
        Paired[] averagesWithSize = new Paired[]{new Paired<Double,Integer>(), new Paired<Double,Integer>()};
        for (int i = 0; i < 2; i++) {
            String calculator = calculators[i];
            List<PercolationRelation> percolationRelations = calculateProgrammingPercolation(calculator);
            int n = percolationRelations.size();
            double d = percolationRelations.stream()
                    .mapToDouble(PercolationRelation::getDistance)
                    .average().orElse(getSize() * getSize());
            averagesWithSize[i].setFirst(d);
            averagesWithSize[i].setSecond(n);
        }
        this.statistic.setPythagorasDistance(averagesWithSize[0]);
        this.statistic.setDiscreteDistance(averagesWithSize[1]);
        return this;
    }

    private int getRedCell(){
        return  (int)getPath().stream().filter(Cell::isWhite).count();
    }

    void calculatePath() {
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.statistic.setRedCellCount(getRedCell());
//        this.distances = lightningBolt.getDistances();

    }

    public List<PercolationRelation> getProgrammings(String distanceCalculatorType) {
//        this.programmings = calculateProgrammingPercolation(distanceCalculatorType);
        return calculateProgrammingPercolation(distanceCalculatorType);
    }

    private List<PercolationRelation> calculateProgrammingPercolation(String distanceCalculatorType) {
        return new PercolationProgramming(matrix, getPath())
                .setDistanceCalculator(DistanceCalculatorTypeResolver.getDistanceCalculator(distanceCalculatorType))
                .getProgrammingPercolationList(this.neighborhood);
    }


 /*   public List<Cell> generateTape(int bound){
        return new Tape(matrix, getPath()).generateTape(bound);
    }*/

    /*public Experiment getDarkRedAndBlackCellsFromWideTape(){
        calculateDarkRedAndBlackCellsInTape();
        return darkRedAndBlackCellsRatio;
        return this;
    }*/

//    private void calculateDarkRedAndBlackCellsInTape(){
//        List<Cell> tape = new Tape(matrix, getPath()).generateWideTape(this.neighborhood);
//        int blackCounter = (int)tape.stream().filter(Cell::isBlack).count();
//        List<Cell> darkRedCells = this.programmings.stream()
//                .map(PercolationRelation::getDarkRedCell)
//                .collect(Collectors.toList());
//        int darkRedCounter = (int)tape.stream().filter(Cell::isBlack).filter(darkRedCells::contains).count();
//        darkRedAndBlackCellsRatio = new Paired<>(darkRedCounter, blackCounter);
//    }

    public Matrix getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        return name;
    }

    public Experiment clear(){
        this.matrix = null;
        this.lightningBolt = null;
        return this;
    }

    public Statistic getStatistic() {
        return statistic;
    }
}