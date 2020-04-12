package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.Statistic;
import com.mostovoy_company.lightning.Pair;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class NewNormalizedStatManager {

    public double clusterCountStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        return statistics.stream()
                .mapToInt(Statistic::getClusterCount)
                .average()
                .orElse(0) / (size * size);
    }

    public double clusterSizeStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        final double[] hasClusterMarkCounter = {0};
        final double[] clusterCounter = {0};
        statistics.forEach(statistic -> {
                    hasClusterMarkCounter[0] += statistic.getBlackCellCount();
                    clusterCounter[0] += statistic.getClusterCount();
                });
        double concentration = hasClusterMarkCounter[0] / (size * size);
        if (clusterCounter[0] > 0) return (concentration) / (clusterCounter[0]);
        else return 0;
    }

    public double redCellsCountStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        return statistics.stream()
                .mapToDouble(Statistic::getRedCellCount)
                .average()
                .orElse(0) / size;
    }

    public double wayLengthStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        return statistics.stream()
                .map(Statistic::getPercolationWayDistance)
                .mapToDouble(d -> d)
                .average()
                .orElse(0) / size;
    }


    public double redCellStationDistanceForPythagoras(List<Statistic> statistics) {
//        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.PYTHAGORAS);
        return 0.0;
    }



    public double redCellStationDistanceForDiscrete(List<Statistic> statistics) {
//        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.DISCRETE);
        return 0.0;
    }


    private double redCellStationDistance(List<Statistic> statistics, String type) {
//        return experiments.stream()
//                .flatMapToDouble(experiment -> experiment.getProgrammings(type).stream()
//                        .mapToDouble(PercolationRelation::getDistance))
//                .average()
//                .orElse(0);
        return 0.0;
    }

    public double darkRedAndBlackCellsRatio(List<Statistic> statistics) {
//        double top = experiments.stream()
//                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getFirst).sum();
//        double bot = experiments.stream()
//                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getSecond).sum();
//        return top / bot;
        return 0.0;
    }
}