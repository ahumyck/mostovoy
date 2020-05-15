package com.mostovoy_company.expirement.chart_experiment.entity;

import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import org.apache.kafka.common.metrics.Stat;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StatisticManager {
    public static double blackCellsTapeStatistic(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getAverageBlackCellsPerRowInWayWidth)
                         .average()
                         .orElse(0);
    }

    public static double clusterCountStatistic(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getClusterCount)
                         .average()
                         .orElse(0);
    }

    public static double clusterSizeStatistic(List<Statistic> statistics) {
        double result = 0;
        final double[] blackCellCounter = {0};
        final double[] clusterCounter = {0};
        statistics.forEach(statistic -> {
            blackCellCounter[0] += statistic.getBlackCellCount();
            clusterCounter[0] += statistic.getClusterCount();
        });
        double concentration = blackCellCounter[0];
        if (clusterCounter[0] > 0) result = concentration / clusterCounter[0];
        return result;
    }

    public static double darkRedCellsTapeStatistic(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getAverageRedCellsPerRowInTapeWidth)
                         .average()
                         .orElse(0);
    }

    public static double interClusterHoleCount(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getInterClustersHoleCount)
                         .average()
                         .orElse(0);
    }

    public static double maxInterClusterHoleCount(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getMaxInterClusterHoleSize)
                         .average()
                         .orElse(0);
    }

    public static double midInterClusterIntervalSize(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getMidInterClustersInterval)
                         .average()
                         .orElse(0);
    }

    public static double percolationThreshold(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToInt(statistic -> statistic.isPercolationizated() ? 1 : 0)
                         .average()
                         .orElse(0);
    }

    public static double percolationWayLength(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getPercolationWayLength)
                         .average()
                         .orElse(0);
    }

    public static double percolationWayWidth(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getPercolationWayWidth)
                         .average()
                         .orElse(0);
    }

    public static double redCellsAddedChart(List<Statistic> statistics) {
        return statistics.stream()
                         .mapToDouble(Statistic::getRedCellCount)
                         .average()
                         .orElse(0);
    }

    public static double redCellsStation(List<Statistic> statistics) {
        AtomicReference<Double> d = new AtomicReference<>(0.0);
        AtomicInteger n = new AtomicInteger(0);
        statistics.stream()
                  .map(Statistic::getPythagorasDistance)
                  .forEach(pair -> {
                      d.updateAndGet(v -> v + pair.getFirst() * pair.getSecond());
                      n.addAndGet(pair.getSecond());
                  });
        if (n.get() == 0) return 0;
        else return d.get() / n.get();
    }

    public static double redCellsStationDiscrete(List<Statistic> statistics) {
        AtomicReference<Double> d = new AtomicReference<>(0.0);
        AtomicInteger n = new AtomicInteger(1);
        statistics.stream()
                  .map(Statistic::getDiscreteDistance)
                  .forEach(pair -> {
                      d.updateAndGet(v -> v + pair.getFirst() * pair.getSecond());
                      n.addAndGet(pair.getSecond());
                  });
        if (n.get() == 0) return 0;
        else return d.get() / n.get();
    }
}
