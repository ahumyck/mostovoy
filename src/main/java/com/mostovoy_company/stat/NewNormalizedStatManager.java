package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.lightning.Pair;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class NewNormalizedStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getSize();
        return experiments.stream()
                .mapToInt(Experiment::getClusterCounter)
                .average()
                .orElse(0) / (size * size);
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        int size = experiments.get(0).getSize();
        final double[] hasClusterMarkCounter = {0};
        final double[] clusterCounter = {0};
        experiments.forEach(experiment -> {
                    hasClusterMarkCounter[0] += experiment.getCountOfBlackCells();
                    clusterCounter[0] += experiment.getClusterCounter();
                });
        double concentration = hasClusterMarkCounter[0] / (size * size);
        if (clusterCounter[0] > 0) return (concentration) / (clusterCounter[0]);
        else return 0;
    }

    public double redCellsCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getSize();
        return experiments.stream()
                .mapToDouble(Experiment::getRedCellsCounter)
                .average()
                .orElse(0) / size;
    }

    public double wayLengthStat(List<Experiment> experiments) {
        int size = experiments.get(0).getSize();
        return experiments.stream()
                .map(Experiment::getDistances)
                .flatMap(Collection::stream)
                .mapToDouble(d -> d)
                .average()
                .orElse(0) / size;
    }

    @Override
    public double redCellStationDistanceForPythagoras(List<Experiment> experiments) {
//        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.PYTHAGORAS);
        return 0.0;
    }


    @Override
    public double redCellStationDistanceForDiscrete(List<Experiment> experiments) {
//        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.DISCRETE);
        return 0.0;
    }


    private double redCellStationDistance(List<Experiment> experiments, String type) {
//        return experiments.stream()
//                .flatMapToDouble(experiment -> experiment.getProgrammings(type).stream()
//                        .mapToDouble(PercolationRelation::getDistance))
//                .average()
//                .orElse(0);
        return 0.0;
    }

    public double darkRedAndBlackCellsRatio(List<Experiment> experiments) {
//        double top = experiments.stream()
//                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getFirst).sum();
//        double bot = experiments.stream()
//                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getSecond).sum();
//        return top / bot;
        return 0.0;
    }
}