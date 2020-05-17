package com.mostovoy_company.expirement.chart_experiment;

import com.mostovoy_company.expirement.chart_experiment.entity.Experiment;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.expirement.chart_experiment.filling.FillingType;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.cost.CostRules;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.neighborhood.NeighborhoodRules;
import com.mostovoy_company.services.ConsumeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ExperimentManager {

    private final NeighborhoodRules neighborhoodRules;
    private final CostRules costRules;

    public ExperimentManager(@Qualifier("defaultNeighborhoodRules") NeighborhoodRules neighborhoodRules,
                             @Qualifier("diagonalCostRules") CostRules costRules) { //diagonalCostRules, defaultCostRules
        this.neighborhoodRules = neighborhoodRules;
        this.costRules = costRules;
    }

    public Experiment initializeSingleExperiment(FillingType fillingType) {
        return new Experiment().matrix(new Matrix(fillingType))
                .setName(fillingType.getName())
                .clusterization()
                .calculateLightningBolt(neighborhoodRules, costRules)
                .calculateProgrammingPercolation()
                .putProgrammingPercolationInStatistic()
                .putBlackAndDarkRedCellsPerTapeInStatistics();
    }

    public List<Experiment> initializeExperimentsParallel(int number, FillingType fillingType) {
        List<Experiment> experimentObservableList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment().matrix(new Matrix(fillingType))
                    .setName(fillingType.getName())
                    .clusterization());
        }
        new Thread(() -> experimentObservableList.parallelStream()
                .forEach(experiment -> experiment.calculateLightningBolt(neighborhoodRules, costRules)
                        .calculateProgrammingPercolation()
                        .putProgrammingPercolationInStatistic()
                        .putBlackAndDarkRedCellsPerTapeInStatistics()
                )).start();
        return experimentObservableList;
    }

    public Experiment initializeExperiment(Matrix matrix, String experimentName) {
        return new Experiment().matrix(matrix)
                .setName(experimentName)
                .clusterization()
                .calculateLightningBolt(neighborhoodRules, costRules)
                .calculateProgrammingPercolation()
                .putProgrammingPercolationInStatistic()
                .putBlackAndDarkRedCellsPerTapeInStatistics();
    }

    public List<Statistic> getStatistics(int count, FillingType fillingType, ConsumeProperties consumeProperties) {
        return Stream.generate(Experiment::new)
                .limit(count)
                .collect(Collectors.toList())
                .parallelStream()
                .map(experiment -> {
                    experiment.matrix(new Matrix(fillingType))
                            .clusterization();
                    if (consumeProperties.isLightningBoltEnable()) {
                        experiment.calculateLightningBolt(neighborhoodRules, costRules)
                                .calculateProgrammingPercolation()
                                .putProgrammingPercolationInStatistic()
                                .putBlackAndDarkRedCellsPerTapeInStatistics();
                    }
                    return experiment.clear().getExperimentStatistic();
                })
                .collect(Collectors.toList());
    }
}