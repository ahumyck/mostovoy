package company.expirement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExperimentManager {


    private ExperimentDataRepository experimentDataRepository = new ExperimentDataRepository();

    public ObservableList<Experiment> initializeExperiments(int number, int size, double probability) {
        experimentDataRepository.clear();
        ObservableList<Experiment> experimentObservableList = FXCollections.observableArrayList();
        for (int i = 0; i < number; i++) {
            experimentObservableList.add(new Experiment("name" + i, "path" + i));
        }
        experimentObservableList.forEach(experiment -> {
            experimentDataRepository.add(experiment.getPath(), new Matrix(size).generateValues(probability).markClusters().joinClusters());
        });
        return experimentObservableList;
    }

    public Matrix getMatrix(Experiment experiment){
        return experimentDataRepository.get(experiment.getPath());
    }
}
