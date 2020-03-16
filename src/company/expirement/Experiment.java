package company.expirement;

import company.entity.Matrix;
import company.lightning.LightningBolt;
import company.lightning.Pair;

import java.util.List;

public class Experiment {

    private String name;
    private Matrix matrix;
    private Pair<List<Pair<Integer, Integer>>, Integer> path = null;


    public Experiment(String name, Matrix matrix) {
        this.name = name;
        this.matrix = matrix;
    }

    public Pair<List<Pair<Integer, Integer>>, Integer> getPath() {
        if (path == null)calculatePath();
        return path;
    }

    private void calculatePath() {
        this.path = LightningBolt.startNextIteration(matrix);
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
