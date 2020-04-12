package com.mostovoy_company.expirement;

import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.filling.FillingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ExperimentManager {


    public ObservableList<Experiment> initializeExperimentsParallel(int number, FillingType fillingType) {
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("Эксперимент №" + (i + 1), new Matrix(fillingType)));
        }
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                experimentObservableList.parallelStream().forEach(Experiment::calculatePath);
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
        experiments.parallelStream().forEach( experiment -> {
            experiment.calculatePath();
            experiment.clear();
        });
        return experiments;
    }
    public List<Statistic> getStatistics(int number, FillingType fillingType){
        List<Statistic> s =
         Stream.generate(Experiment::new)
                .limit(number)
                .parallel()
                .map(experiment -> experiment.matrix(new Matrix(fillingType)))
                .map(Experiment::calculateLightningBolt)
                .map(Experiment::getStatistic)
                .collect(Collectors.toList());
        log.info("=> statistics: " + s);
         return s;
    }
}
