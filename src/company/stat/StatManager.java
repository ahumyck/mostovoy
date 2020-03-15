package company.stat;

import company.entity.Cell;
import company.entity.Matrix;

import java.util.List;
import java.util.OptionalDouble;

public class StatManager {

    public double clusterCountStat(List<Matrix> matrices) {
        int size = matrices.get(0).getSize();
        OptionalDouble midClusterCount = matrices.stream().mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.getAsDouble() / (size * size);
    }

    public double clusterSizeStat(List<Matrix> matrices) {
        int size = matrices.get(0).getSize();
        return matrices.stream()
                .mapToDouble(matrix -> ((double) matrix.stream().filter(Cell::hasClusterMark).count()) / matrix.getClusterCounter())
                .average()
                .getAsDouble() / (size * size);
    }

}
