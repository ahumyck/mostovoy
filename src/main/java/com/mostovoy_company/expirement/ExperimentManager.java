package com.mostovoy_company.expirement;

import com.mostovoy_company.analyzer.AnalyzerModule;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.services.ConsumeProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ExperimentManager {

    public ObservableList<Experiment> initializeExperimentsParallel(int number, FillingType fillingType) {
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("Эксперимент №" + (i + 1), new Matrix(fillingType)));
        }
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                experimentObservableList.parallelStream().forEach(experiment -> {
                    experiment.calculatePath();
                    experiment.putPercolationProgrammingInStats();
                });
                return null;
            }
        }).start();
        return experimentObservableList;
    }

    public List<Experiment> initializeExperiments(int number, FillingType fillingType) {
        List<Experiment> experiments = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            experiments.add(new Experiment("Эксперимент №" + (i + 1), new Matrix(fillingType)));
        }
        experiments.parallelStream().forEach(experiment -> {
            experiment.calculatePath();
            experiment.putPercolationProgrammingInStats();
            experiment.clear();
        });
        return experiments;
    }

    public List<Statistic> getStatistics(int count, FillingType fillingType, ConsumeProperties consumeProperties) {
        return Stream.generate(Experiment::new)
                .limit(count)
                .collect(Collectors.toList())
                .parallelStream()
                .map(experiment -> {
                    experiment.matrix(new Matrix(fillingType));
                    if (consumeProperties.isLightningBoltEnable()) {
                        experiment
                                .calculateLightningBolt()
                                .putPercolationProgrammingInStats();
                    }
                    return experiment.clear().getStatistic();
                })
                .collect(Collectors.toList());
    }
}