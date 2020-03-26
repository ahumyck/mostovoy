package company.expirement;

import company.entity.Matrix;
import company.lightning.LightningBolt;
import company.lightning.Pair;

import java.util.Collections;
import java.util.List;

public class Experiment {

    private String name;
    private Matrix matrix;
    private Pair<List<Pair<Integer, Integer>>, Integer> path = null;
    private int redCellsCounter;


    public Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
    }

    public Pair<List<Pair<Integer, Integer>>, Integer> getPath() {
        return path != null ? path : new Pair<>(Collections.emptyList(), 0);
    }

    public int getRedCellsCounter() {
        return redCellsCounter;
    }

    public int getDistance(){
        return path != null ? path.getFirst().size() : 0;
    }

    void calculatePath() {
        LightningBolt lightningBolt = new LightningBolt(matrix);
        this.path = lightningBolt.findShortestWay();
        this.redCellsCounter = lightningBolt.getRedCellCounter();
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
