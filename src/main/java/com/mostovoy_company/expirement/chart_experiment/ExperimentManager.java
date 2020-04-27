package com.mostovoy_company.expirement.chart_experiment;

import com.mostovoy_company.expirement.chart_experiment.entity.Experiment;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.expirement.chart_experiment.filling.FillingType;
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

    private NeighborhoodRules rules;

    public ExperimentManager(@Qualifier("defaultRules") NeighborhoodRules rules) {
        this.rules = rules;
    }

    public List<Experiment> initializeExperimentsParallel(int number, FillingType fillingType) {
        List<Experiment> experimentObservableList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment().matrix(new Matrix(fillingType))
                    .name(fillingType.getName())
                    .clusterization());
        }
        new Thread(() -> experimentObservableList.parallelStream()
                .forEach(experiment -> experiment.calculateLightningBolt(rules)
                        .calculateProgrammingPercolation()
                        .putProgrammingPercolationInStatistic()
                        .putBlackAndDarkRedCellsPerTapeInStatistics()
                )).start();
        return experimentObservableList;
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
                        experiment.calculateLightningBolt(rules)
                                .calculateProgrammingPercolation()
                                .putProgrammingPercolationInStatistic()
                                .putBlackAndDarkRedCellsPerTapeInStatistics();
                    }
                    return experiment.clear().getExperimentStatistic();
                })
                .collect(Collectors.toList());
    }
}