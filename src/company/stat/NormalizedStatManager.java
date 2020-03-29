package company.stat;

import company.entity.Cell;
import company.entity.Matrix;
import company.expirement.Experiment;

import java.util.List;
import java.util.OptionalDouble;

public class NormalizedStatManager implements StatManager {

    public double clusterCountStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize();
        OptionalDouble midClusterCount = experiments.stream().map(Experiment::getMatrix).mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.getAsDouble() / (size*size) ;
    }

    public double clusterSizeStat(List<Experiment> experiments) {
        int size = experiments.get(0).getMatrix().getSize();
        return experiments.stream()
                .map(Experiment::getMatrix)
                .mapToDouble(matrix -> ((double) matrix.stream().filter(Cell::hasClusterMark).count()) / matrix.getClusterCounter())
                .average()
                .getAsDouble() / size ;
    }

    public double redCellsCountStat(List<Experiment> experiments){
        int size = experiments.get(0).getMatrix().getSize();
        return experiments.stream().mapToDouble(Experiment::getRedCellsCounter).average().getAsDouble()/ size ;

    }

    public double wayLengthStat(List<Experiment> experiments){
        int size = experiments.get(0).getMatrix().getSize();
        return experiments.stream().mapToDouble(Experiment::getDistance).average().getAsDouble()/size ;
    }

}
