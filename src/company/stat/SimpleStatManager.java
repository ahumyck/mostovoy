package company.stat;

import company.entity.Cell;
import company.entity.Matrix;
import company.expirement.Experiment;

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
    public double redCellStationDistanceForPifagor(List<Experiment> experiments) {
        return 0;
    }

    @Override
    public double redCellStationDistanceForNePifagor(List<Experiment> experiments) {
        return 0;
    }

}
