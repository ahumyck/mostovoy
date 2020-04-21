package com.mostovoy_company.expirement.entity;

import com.mostovoy_company.expirement.lightningbolt.LightningBolt;
import com.mostovoy_company.expirement.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.expirement.programminPercolation.percolation.PercolationProgramming;
import com.mostovoy_company.expirement.programminPercolation.percolation.PercolationRelation;
import com.mostovoy_company.expirement.lightningbolt.Paired;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class Experiment {

    private final static String DEFAULT_NAME = "noname";
    private Statistic statistic = new Statistic();
    private String name;
    private Matrix matrix;
    private List<Cell> percolationWay;
    private LightningBolt lightningBolt;

    public Experiment matrix(Matrix matrix) {
        this.matrix = matrix;
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        return this;
    }

    public Experiment name(String name) {
        this.name = name;
        return this;
    }

    public Experiment clusterization(){
        this.matrix.clusterization();
        this.statistic.setBlackCellCount((int)matrix.getCountOfBlackCells());
        this.statistic.setClusterCount(matrix.getClusterCount());
        return this;
    }


    public Experiment calculateLightningBolt() {
        this.lightningBolt = new LightningBolt(this.matrix);
        Paired<List<Cell>, Integer> listIntegerPaired = lightningBolt.calculateShortestPaths().getShortestPath().get();
        percolationWay = listIntegerPaired.getFirst();
        this.statistic.setRedCellCount(getRedCellsCount());
        this.statistic.setPercolationizated(getRedCellsCount() == 0);
        this.statistic.setPercolationWayWidth(percolationWayWidth());
        this.statistic.setPercolationWayLength(lightningBolt.getDistanceForShortestPath());
        calculateInterClusterIntervalSizes();
        return this;
    }

    public String getName() {
        return Optional.of(name).orElse(getSize() + "x" + getSize());
    }

    public int getSize() {
        return this.statistic.getSize();
    }

    public List<Cell> getPercolationWay() {
        return Optional.of(percolationWay).orElseThrow(() -> new RuntimeException("NO PERCOLATION WAY UNDER EXPERIMENT"));
    }

    public Matrix getMatrix() {
        return Optional.of(matrix).orElseThrow(() -> new RuntimeException("NO MATRIX UNDER EXPERIMENT"));
    }

    private void calculateInterClusterIntervalSizes() {
        AtomicInteger interClusterHoleCount = new AtomicInteger(0);
        List<AtomicInteger> interClustersSizes = new ArrayList<>();
        interClustersSizes.add(new AtomicInteger());
        getPercolationWay().forEach(cell -> {
            if (cell.isWhite()) {
                interClustersSizes.get(interClusterHoleCount.get()).incrementAndGet();
            } else if (cell.isBlack() && interClustersSizes.get(interClusterHoleCount.get()).get() != 0) {
                interClustersSizes.add(new AtomicInteger(0));
                interClusterHoleCount.incrementAndGet();
            }
        });
        if (getPercolationWay().get(getPercolationWay().size() - 1).isBlack()) {
            interClustersSizes.remove(interClustersSizes.get(interClustersSizes.size() - 1));
        }
        this.statistic.setMidInterClustersInterval(interClustersSizes.stream().mapToDouble(AtomicInteger::get).average().orElse(0));
        this.statistic.setInterClustersHoleCount(interClustersSizes.size());
    }

    private int percolationWayWidth() {
        int max = getPercolationWay().stream().mapToInt(Cell::getY).max().getAsInt();
        int min = getPercolationWay().stream().mapToInt(Cell::getY).min().getAsInt();
        return max - min + 1;
    }

    public Experiment calculateProgramingPercolation() {
        String[] calculators = {DistanceCalculatorTypeResolver.PYTHAGORAS, DistanceCalculatorTypeResolver.DISCRETE};
        Paired[] averagesWithSize = new Paired[]{new Paired<Double, Integer>(), new Paired<Double, Integer>()};
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

    private int getRedCellsCount() {
        return (int) percolationWay.stream().filter(Cell::isWhite).count();
    }

    public List<PercolationRelation> getProgrammings(String distanceCalculatorType) {
        return calculateProgrammingPercolation(distanceCalculatorType);
    }

    private List<PercolationRelation> calculateProgrammingPercolation(String distanceCalculatorType) {
        int neighborhood = 2 * (matrix.getSize() - 2 * Matrix.OFFSET);
        return new PercolationProgramming(matrix, percolationWay)
                .setDistanceCalculator(DistanceCalculatorTypeResolver.getDistanceCalculator(distanceCalculatorType))
                .getProgrammingPercolationList(neighborhood);
    }

    @Override
    public String toString() {
        return getName();
    }

    public Experiment clear() {
        this.matrix = null;
        this.lightningBolt = null;
        return this;
    }

    public Statistic getExperimentStatistic() {
        return statistic;
    }
}