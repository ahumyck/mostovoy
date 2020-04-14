package com.mostovoy_company.stat;

import com.mostovoy_company.expirement.Statistic;
import com.mostovoy_company.lightning.Paired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class NewNormalizedStatManager {

    public double clusterCountStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        return statistics.stream()
                .mapToDouble(Statistic::getClusterCount)
                .average()
                .orElse(0) / (size * size);
    }

    public double clusterSizeStat(List<Statistic> statistics) {
        int size = statistics.get(0).getSize();
        final double[] blackCellCounter = {0};
        final double[] clusterCounter = {0};
        statistics.forEach(statistic -> {
                    blackCellCounter[0] += statistic.getBlackCellCount();
                    clusterCounter[0] += statistic.getClusterCount();
                });
        double concentration = blackCellCounter[0] / (size * size);
        if (clusterCounter[0] > 0) return (concentration) / (clusterCounter[0]);
        else return 0;
    }

    public double predictConcentration(List<Statistic> statistics){
        int size = statistics.get(0).getSize();
        final double[] blackCellCounter = {0};
        statistics.forEach(statistic -> {
            blackCellCounter[0] += statistic.getBlackCellCount();
        });
        return blackCellCounter[0] / (size * size);
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
        AtomicReference<Double> d = new AtomicReference<>(0.0);
        AtomicInteger n = new AtomicInteger();
        statistics.stream()
                .map(Statistic::getPercolationProgramming)
                .map(Paired::getFirst)
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
                .map(Statistic::getPercolationProgramming)
                .map(Paired::getSecond)
                .forEach(pair ->{
                    d.updateAndGet(v -> v + pair.getFirst() * pair.getSecond());
                    n.addAndGet(pair.getSecond());
                });
        return d.get()/n.get();
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