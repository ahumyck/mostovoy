package com.mostovoy_company.expirement.chart_experiment.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.LightningBolt;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.adjacency.neighborhood.NeighborhoodRules;
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
import java.util.stream.Collectors;

@NoArgsConstructor
public class Experiment {

    @Expose(deserialize = false, serialize = false)
    private LightningBolt lightningBolt;
    @Expose(deserialize = false, serialize = false)
    private List<PercolationRelation> relations;

    @Expose
    private Statistic statistic = new Statistic();
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

    public Experiment name(String name) {
        this.name = name;
        return this;
    }

    public Experiment clusterization() {
        this.matrix.clusterization();
        this.statistic.setBlackCellCount((int) matrix.getCountOfBlackCells());
        this.statistic.setClusterCount(matrix.getClusterCount());
        return this;
    }

    public Experiment calculateLightningBolt(NeighborhoodRules rules) {
        this.lightningBolt = new LightningBolt(this.matrix, rules);
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

        if (max - min > 0) {
            List<Integer> blackPerTape = new ArrayList<>();
            List<Integer> darkRedPerTape = new ArrayList<>();


            List<Cell> darkredCells = getProgrammings().stream()
                    .map(PercolationRelation::getDarkRedCell).collect(Collectors.toList());

            for (int i = Matrix.OFFSET; i < matrix.getSize() - Matrix.OFFSET; i++) {
                int index = i - Matrix.OFFSET;
                blackPerTape.add(0);
                darkRedPerTape.add(0);
                for (int column = min; column <= max; column++) {
                    Cell cell = matrix.getCell(i, column);
                    if (cell.isBlack() && !this.percolationWay.contains(cell)) {
                        blackPerTape.set(index, blackPerTape.get(index) + 1);
                        if (darkredCells.contains(cell))
                            darkRedPerTape.set(index, darkRedPerTape.get(index) + 1);
                    }
                }
            }

            this.statistic.setAverageDarkRedCellsInTape(darkRedPerTape.stream().mapToDouble(d -> d).average().orElse(0));
            this.statistic.setAverageBlackCellsInTape(blackPerTape.stream().mapToDouble(d -> d).average().orElse(0));
        } else {
            this.statistic.setAverageDarkRedCellsInTape(0);
            this.statistic.setAverageBlackCellsInTape(0);
        }
        return this;
    }

    private int percolationWayWidth() {
        int max = getPercolationWay().stream().mapToInt(Cell::getY).max().getAsInt();
        int min = getPercolationWay().stream().mapToInt(Cell::getY).min().getAsInt();
        return max - min + 1;
    }

    public Experiment putProgrammingPercolationInStatistic() {
        List<PercolationRelation> percolationRelations = new PercolationProgramming(matrix, percolationWay)
                .getProgrammingPercolationList(2 * (matrix.getSize() - 2 * Matrix.OFFSET));
        this.statistic.setMidDarkRedCellsStation(percolationRelations.stream()
                .mapToDouble(PercolationRelation::getDistance)
                .average().orElse(getSize() * getSize()));
        this.statistic.setRelationsCounter(percolationRelations.size());
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