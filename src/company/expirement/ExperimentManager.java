package company.expirement;

import company.entity.Matrix;
import company.filling.FillingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ExperimentManager {


    private ExperimentDataRepository experimentDataRepository = new ExperimentDataRepository();

    public ObservableList<Experiment> initializeExperiments(int number, FillingType fillingType) {
        experimentDataRepository.clear();
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("Эксперимент №" + (i + 1), "key" + i));
        }
        experimentObservableList.forEach(experiment -> {
            experimentDataRepository.add(experiment.getPath(), new Matrix(fillingType));
        });
        return experimentObservableList;
    }

    public List<Matrix> getMatrices(int number, FillingType fillingType){
        List<Matrix> matrices = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            matrices.add(new Matrix(fillingType));
        }
        return matrices;
    }

    public Matrix getMatrix(Experiment experiment){
        return experimentDataRepository.get(experiment.getPath());
    }
}
