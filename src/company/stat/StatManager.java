package company.stat;

import company.entity.Cell;
import company.entity.Matrix;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;

import static java.util.stream.Collectors.averagingDouble;

public class StatManager {

    class Result {
        double midClusterCount;
        double midClusterSize;

        public Result(double midClusterCount, double midClusterSize) {
            this.midClusterCount = midClusterCount;
            this.midClusterSize = midClusterSize;
        }

        public double getMidClusterCount() {
            return midClusterCount;
        }

        public double getMidClusterSize() {
            return midClusterSize;
        }
    }

    public double clusterCountStat(List<Matrix> matrices) {
        OptionalDouble midClusterCount = matrices.stream().mapToInt(Matrix::getClusterCounter).average();
        return midClusterCount.getAsDouble();
    }

    public double clusterSizeStat(List<Matrix> matrices)
    {
       return matrices.stream()
                .mapToDouble(matrix -> ((double)matrix.stream().filter(Cell::hasClusterMark).count()) / matrix.getClusterCounter())
                .average()
                .getAsDouble();
    }

}
