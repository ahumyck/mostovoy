package com.mostovoy_company.expirement.chart_experiment.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.LightningBolt;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost.CostRules;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.neighborhood.NeighborhoodRules;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.percolation.PercolationProgramming;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.percolation.PercolationRelation;
import lombok.NoArgsConstructor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class Experiment {

    @Expose(deserialize = false, serialize = false)
    private LightningBolt lightningBolt;
    @Expose(deserialize = false, serialize = false)
    private List<PercolationRelation> relations;

    @Expose
    private final Statistic statistic = new Statistic();
    @Expose
    private String name;
    @Expose
    private Matrix matrix;
    @Expose
    private List<Cell> percolationWay;

    public static Experiment getExperimentFromJson(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        JsonReader jsonReader = new JsonReader(fileReader);
        Experiment experiment = new Gson().fromJson(jsonReader, Experiment.class);
        fileReader.close();
        jsonReader.close();
        return experiment;
    }

    public Experiment matrix(Matrix matrix) {
        this.matrix = matrix;
        this.statistic.setSize(matrix.getSize() - 2 * Matrix.OFFSET);
        return this;
    }

    public Experiment clusterization() {
        new HoshenKopelman().clusterization(matrix);
        this.statistic.setBlackCellCount((int) matrix.getCountOfBlackCells());
        this.statistic.setClusterCount(matrix.getClusterCount());
        return this;
    }

    public Experiment calculateLightningBolt(NeighborhoodRules neighborhoodRules, CostRules costRules) {
        this.lightningBolt = new LightningBolt(this.matrix, neighborhoodRules, costRules);
        Paired<List<Cell>, Integer> listIntegerPaired = this.lightningBolt.calculateShortestPaths().getShortestPath().get();
        this.percolationWay = listIntegerPaired.getFirst();
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

    public Experiment setName(String name) {
        this.name = name;
        return this;
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
        this.statistic.setMaxInterClusterHoleSize(interClustersSizes.stream().mapToDouble(AtomicInteger::get).max().orElse(0));
        this.statistic.setInterClustersHoleCount(interClustersSizes.size());
    }

    public Experiment putBlackAndDarkRedCellsPerTapeInStatistics() {
        int max = getPercolationWay().stream().mapToInt(Cell::getY).max().getAsInt() + Matrix.OFFSET;
        int min = getPercolationWay().stream().mapToInt(Cell::getY).min().getAsInt() + Matrix.OFFSET;

        if (max - min >= 0) {
            List<Integer> blackPerRowInTape = new ArrayList<>();
            List<Integer> redPerRowInTape = new ArrayList<>();


            for (int i = Matrix.OFFSET; i < matrix.getSize() - Matrix.OFFSET; i++) {
                int index = i - Matrix.OFFSET;
                blackPerRowInTape.add(0);
                redPerRowInTape.add(0);
                for (int column = min; column <= max; column++) {
                    Cell cell = matrix.getCell(i, column);
                    if (cell.isBlack()) blackPerRowInTape.set(index, blackPerRowInTape.get(index) + 1);
                    if (cell.isWhite() && this.percolationWay.contains(cell))
                        redPerRowInTape.set(index, redPerRowInTape.get(index) + 1);
                }
            }

            this.statistic.setAverageRedCellsPerRowInTapeWidth(redPerRowInTape.stream().mapToDouble(d -> d).average().orElse(0));
            this.statistic.setAverageBlackCellsPerRowInWayWidth(blackPerRowInTape.stream().mapToDouble(d -> d).average().orElse(0));
        } else {
            this.statistic.setAverageRedCellsPerRowInTapeWidth(0);
            this.statistic.setAverageBlackCellsPerRowInWayWidth(0);
        }
        return this;
    }

    private int percolationWayWidth() {
        int max = getPercolationWay().stream().mapToInt(Cell::getY).max().getAsInt();
        int min = getPercolationWay().stream().mapToInt(Cell::getY).min().getAsInt();
        return max - min + 1;
    }

    public Experiment putProgrammingPercolationInStatistic() {
        String[] calculators = {DistanceCalculatorTypeResolver.PYTHAGORAS, DistanceCalculatorTypeResolver.DISCRETE};
        Paired<Double, Integer>[] averagesWithSize = new Paired[]{new Paired<Double, Integer>(), new Paired<Double, Integer>()};
        for (int i = 0; i < 2; i++) {
            String calculator = calculators[i];
            List<PercolationRelation> percolationRelations = new PercolationProgramming(matrix, percolationWay)
                    .setDistanceCalculator(DistanceCalculatorTypeResolver.getDistanceCalculator(calculator))
                    .getProgrammingPercolationList(2 * (matrix.getSize() - 2 * Matrix.OFFSET));
            int n = percolationRelations.size();
            double d = percolationRelations.stream()
                                           .mapToDouble(PercolationRelation::getDistance)
                                           .average().orElse(0);
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

    public List<PercolationRelation> getProgrammings() {
        return Optional.of(this.relations).orElseThrow(() -> new RuntimeException("NO RELATIONS UNDER PERCOLATION WAY"));
    }

    public Experiment calculateProgrammingPercolation() {
        int neighborhood = 2 * (matrix.getSize() - 2 * Matrix.OFFSET);
        this.relations = new PercolationProgramming(matrix, percolationWay)
                .getProgrammingPercolationList(neighborhood);
        return this;
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

    public void saveExperimentToJson(String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create().toJson(this, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}