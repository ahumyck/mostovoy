package company.expirement;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.LightningBolt;
import company.lightning.Pair;
import company.programminPercolation.PercolationProgramming;
import company.programminPercolation.PercolationRelation;
import company.programminPercolation.distance.DistanceCalculatorTypeResolver;

import java.util.List;

public class Experiment {

    private String name;
    private Matrix matrix;
    private Pair<List<Cell>, Integer> path = null;
    private Integer redCellsCounter;
    private List<PercolationRelation> programmings = null;

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }


    public Experiment(String name) {
        this.name = name;
    }

    public Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
    }

    public List<Cell> getPath() {
        if (path == null) calculatePath();
        return path.getFirst();

    }

    public int getRedCellsCounter() {
        return redCellsCounter;
    }

    public int getDistance() {
        return path != null ? path.getFirst().size() : 0;
    }

    public void calculatePath() {
        LightningBolt lightningBolt = new LightningBolt(matrix);
//        System.out.println(System.currentTimeMillis() - startTime);
//        startTime = System.currentTimeMillis();
//        long startTime = System.currentTimeMillis();
        this.path = lightningBolt.calculateShortestPaths().getShortestPath().get();
//        System.out.println("    " + (System.currentTimeMillis() - startTime));
        this.redCellsCounter = lightningBolt.getRedCellCounterForShortestPath();
    }

    public List<PercolationRelation> getProgrammings(String distanceCalculatorType) {
        calculateProgrammingPercolation(distanceCalculatorType);
        return programmings;
    }

    void calculateProgrammingPercolation(String distanceCalculatorType) {
        this.programmings = new PercolationProgramming(matrix, getPath())
                .setDistanceCalculator(DistanceCalculatorTypeResolver.getDistanceCalculator(distanceCalculatorType)).getProgrammingPercolationList();
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
