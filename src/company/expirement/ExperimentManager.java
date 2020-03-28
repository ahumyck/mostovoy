package company.expirement;

import company.entity.Matrix;
import company.filling.FillingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class ExperimentManager {


    public ObservableList<Experiment> initializeExperimentsParallel(int number, FillingType fillingType) {
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("Эксперимент №" + (i + 1), new Matrix(fillingType)));
        }
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                experimentObservableList.parallelStream().forEach(Experiment::calculatePath);
                return null;
            }
        }).start();
        return experimentObservableList;
    }

    public ObservableList<Experiment> initializeExperiments(int number, FillingType fillingType) {
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("Эксперимент №" + (i + 1), new Matrix(fillingType)));
        }
        experimentObservableList.parallelStream().forEach(Experiment::calculatePath);
        return experimentObservableList;
    }

    public List<Matrix> getMatrices(int number, FillingType fillingType) {
        List<Matrix> matrices = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            matrices.add(new Matrix(fillingType));
        }
        return matrices;
    }
}
