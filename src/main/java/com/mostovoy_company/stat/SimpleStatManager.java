package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.lightning.Paired;

import java.util.List;
import java.util.OptionalDouble;

public class SimpleStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        OptionalDouble midClusterCount = experiments.stream().map(Experiment::getMatrix).mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.orElse(0);
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToDouble(matrix -> ((double) matrix.stream().filter(Cell::hasClusterMark).count()) / matrix.getClusterCounter())
                .average()
                .orElse(0);
    }

    public double redCellsCountStat(List<Experiment> experiments){
        return experiments.stream().mapToDouble(Experiment::getRedCellsCounter).average().orElse(0);

    }

    public double wayLengthStat(List<Experiment> experiments){
        return experiments.stream().mapToDouble(Experiment::getDistance).average().orElse(0);
    }

    @Override
    public double redCellStationDistanceForPythagoras(List<Experiment> experiments) {
        return 0;
    }

    @Override
    public double redCellStationDistanceForDiscrete(List<Experiment> experiments) {
        return 0;
    }

    @Override
    public double darkRedAndBlackCellsRatio(List<Experiment> experiments) {
        return 0;
    }

}
