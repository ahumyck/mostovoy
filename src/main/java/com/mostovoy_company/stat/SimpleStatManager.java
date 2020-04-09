package com.mostovoy_company.stat;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.lightning.Pair;

import java.util.List;
import java.util.OptionalDouble;

public class SimpleStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        OptionalDouble midClusterCount = experiments.stream().map(Experiment::getMatrix).mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.getAsDouble();
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToDouble(matrix -> ((double) matrix.stream().filter(Cell::hasClusterMark).count()) / matrix.getClusterCounter())
                .average()
                .getAsDouble();
    }

    public double redCellsCountStat(List<Experiment> experiments){
        return experiments.stream().mapToDouble(Experiment::getRedCellsCounter).average().getAsDouble();

    }

    public double wayLengthStat(List<Experiment> experiments){
        return experiments.stream().mapToDouble(Experiment::getDistance).average().getAsDouble();
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
        double top = experiments.stream()
                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getFirst).sum();
        double bot = experiments.stream()
                .map(Experiment::getDarkRedAndBlackCellsFromWideTape).mapToInt(Pair::getSecond).sum();
        return top/bot;
    }

}
