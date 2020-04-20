package com.mostovoy_company.stat;

import com.mostovoy_company.expirement.Statistic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StatisticManager {

    public double clusterCountStat(List<Statistic> statistics) {
        return statistics.stream()
                .mapToDouble(Statistic::getClusterCount)
                .average()
                .orElse(0)/* / (size * size)*/;
    }

    public double clusterSizeStat(List<Statistic> statistics) {
        final double[] blackCellCounter = {0};
        final double[] clusterCounter = {0};
        statistics.forEach(statistic -> {
                    blackCellCounter[0] += statistic.getBlackCellCount();
                    clusterCounter[0] += statistic.getClusterCount();
                });
        double concentration = blackCellCounter[0] /*/ (size * size)*/;
        if (clusterCounter[0] > 0) return (concentration) / (clusterCounter[0]);
        else return 0;
    }

    public double predictConcentration(List<Statistic> statistics){
        final double[] blackCellCounter = {0};
        statistics.forEach(statistic -> {
            blackCellCounter[0] += statistic.getBlackCellCount();
        });
        return blackCellCounter[0] / /*(size * size **/ statistics.size();
    }

    public double redCellsCountStat(List<Statistic> statistics) {
        return statistics.stream()
                .mapToDouble(Statistic::getRedCellCount)
                .average()
                .orElse(0) /*/ size*/;
    }

    public double wayLengthStat(List<Statistic> statistics) {
        return statistics.stream()
                .mapToDouble(Statistic::getPercolationWayDistance)
                .average()
                .orElse(0) /*/ size*/;
    }

    public double percolationThreshold(List<Statistic> statistics){
        return statistics.stream()
                .mapToInt(statistic -> statistic.isPercolationizated() ? 1 : 0)
                .average()
                .orElse(0);
    }

    public double percolationWayWidth(List<Statistic> statistics){
        return statistics.stream().mapToDouble(Statistic::getPercolationWayWidth).average().orElse(0);
    }


    public double redCellStationDistanceForPythagoras(List<Statistic> statistics) {
        AtomicReference<Double> d = new AtomicReference<>(0.0);
        AtomicInteger n = new AtomicInteger();
        statistics.stream()
                .map(Statistic::getPythagorasDistance)
                .forEach(pair ->{
                    d.updateAndGet(v -> v + pair.getFirst() * pair.getSecond());
                    n.addAndGet(pair.getSecond());
                });
        return d.get()/n.get();
    }



    public double redCellStationDistanceForDiscrete(List<Statistic> statistics) {
        AtomicReference<Double> d = new AtomicReference<>( 0.0);
        AtomicInteger n = new AtomicInteger();
        statistics.stream()
                .map(Statistic::getDiscreteDistance)
                .forEach(pair ->{
                    d.updateAndGet(v -> v + pair.getFirst() * pair.getSecond());
                    n.addAndGet(pair.getSecond());
                });
        return d.get()/n.get();
    }

    public double interClustersHoleCount(List<Statistic> statistics){
        return statistics.stream().mapToDouble(Statistic::getInterClustersHoleCount).average().orElse(0);
    }

    public double midInterClustersIntervalSize(List<Statistic> statistics){
        return statistics.stream().mapToDouble(Statistic::getMidInterClustersInterval).average().orElse(0);
    }
}