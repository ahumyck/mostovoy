package com.mostovoy_company.stat;

import com.mostovoy_company.expirement.Experiment;

import java.util.List;

public interface StatManager {

    double clusterCountStat(List<Experiment> experiments);

    double clusterSizeStat(List<Experiment> experiments);

    double redCellsCountStat(List<Experiment> experiments);

    double wayLengthStat(List<Experiment> experiments);

    double redCellStationDistanceForPythagoras(List<Experiment> experiments);

    double redCellStationDistanceForDiscrete(List<Experiment> experiments);

    double darkRedAndBlackCellsRatio(List<Experiment> experiments);
}
