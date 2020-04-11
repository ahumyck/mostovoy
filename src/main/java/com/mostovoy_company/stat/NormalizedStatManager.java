package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.lightning.Pair;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Component
@Slf4j
public class NormalizedStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .map(Experiment::getMatrix)
//                .filter(matrix -> matrix.getClusterCounter() > 0)
                .mapToInt(Matrix::getClusterCounter)
                .average()
                .orElse(0) / (size * size);
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        final double[] hasClusterMarkCounter = {0};
        final double[] clusterCounter = {0};
        experiments.stream()
                .map(Experiment::getMatrix)
                .filter(matrix -> matrix.getClusterCounter() > 0)
                .forEach(matrix -> {
                        hasClusterMarkCounter[0] += matrix.stream().filter(Cell::hasClusterMark).count();
                        clusterCounter[0] += matrix.getClusterCounter();
                });
        double concentration = hasClusterMarkCounter[0]/(size*size);
        if(clusterCounter[0] > 0) return (concentration)/(clusterCounter[0]);
        else return 0;
    }

    public double redCellsCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .mapToDouble(Experiment::getRedCellsCounter)
                .average()
                .orElse(0) / size;

    }

    public double wayLengthStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .map(Experiment::getDistances)
                .flatMap(Collection::stream)
                .mapToDouble(d -> d)
                .average()
                .orElse(0)/size;
    }

    @Override
    public double redCellStationDistanceForPythagoras(List<Experiment> experiments) {
        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.PYTHAGORAS);
    }


    @Override
    public double redCellStationDistanceForDiscrete(List<Experiment> experiments) {
        return redCellStationDistance(experiments, DistanceCalculatorTypeResolver.DISCRETE);
    }


    private double redCellStationDistance(List<Experiment> experiments, String type) {
        return experiments.stream()
                .flatMapToDouble(experiment -> experiment.getProgrammings(type).stream()
                        .mapToDouble(PercolationRelation::getDistance))
                .average()
                .orElse(0);
    }

    public double darkRedAndBlackCellsRatio(List<Experiment> experiments) {
        double top = experiments.stream()
                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getFirst).sum();
        double bot = experiments.stream()
                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getSecond).sum();
        return top / bot;
    }

}
