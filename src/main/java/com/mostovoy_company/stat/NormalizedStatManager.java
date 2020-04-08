package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.programminPercolation.PercolationRelation;
import com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class NormalizedStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToInt(Matrix::getClusterCounter)
                .average()
                .orElse(0) / (size * size);
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToDouble(matrix -> {
                    if (matrix.getClusterCounter() > 0)
                        return (double) matrix.stream().filter(Cell::hasClusterMark).count() / matrix.getClusterCounter();
                    else return 0;
                })
                .average()
                .orElse(0) / (size * size) ;
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
                .mapToDouble(Experiment::getDistance)
                .average()
                .orElse(0) / size;
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

}
