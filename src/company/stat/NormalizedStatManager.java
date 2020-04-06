package company.stat;

import company.entity.Cell;
import company.entity.Matrix;
import company.expirement.Experiment;
import company.programminPercolation.PercolationRelation;

import java.util.List;
import java.util.OptionalDouble;

import static company.programminPercolation.DistancePercolationTypeResolver.NE_PIFAGOR;
import static company.programminPercolation.DistancePercolationTypeResolver.PIFAGOR;

public class NormalizedStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        OptionalDouble midClusterCount = experiments.stream().map(Experiment::getMatrix).mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.getAsDouble() / (size * size);
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToDouble(matrix -> {
                    if (matrix.getClusterCounter() > 0)
                        return (double) matrix.stream().filter(Cell::hasClusterMark).count() / matrix.getClusterCounter();
                    else return 0;
                }).average().orElse(0) / (size * size);
    }

    public double redCellsCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream().mapToDouble(Experiment::getRedCellsCounter).average().getAsDouble() / size;

    }

    public double wayLengthStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize() - 2 * Matrix.OFFSET;
        return experiments.stream().mapToDouble(Experiment::getDistance).average().getAsDouble() / size;
    }

    @Override
    public double redCellStationDistanceForPifagor(List<Experiment> experiments) {
        double res = redCellStationDistance(experiments, PIFAGOR);
        System.out.println("res: " + res);
        return res;
    }


    @Override
    public double redCellStationDistanceForNePifagor(List<Experiment> experiments) {
        return redCellStationDistance(experiments, NE_PIFAGOR);
    }


    public double redCellStationDistance(List<Experiment> experiments, String type) {
        return experiments.stream().flatMapToDouble(experiment -> experiment.getProgrammings(type).stream().mapToDouble(PercolationRelation::getDistance)).average().orElse(0);
    }

}
