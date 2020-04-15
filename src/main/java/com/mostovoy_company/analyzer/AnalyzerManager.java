package com.mostovoy_company.analyzer;

import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.filling.RandomFillingType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AnalyzerManager {

    @Autowired
    private AnalyzerModule analyzerModule;

    public ObservableList<AnalyzerData> initializeAnalyzerExperimentsParallel(int number, int matrixSize, int probability) {
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setSize(matrixSize);
        fillingType.setPercolationProbability(probability);
        ObservableList<Matrix> matricesObservableList = FXCollections.observableArrayList();
        ObservableList<AnalyzerData> analyzerDataObservableList = FXCollections.observableArrayList();

        for (int i = 0; i < number; i++) {
            matricesObservableList.add(new Matrix(fillingType));
        }

        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                matricesObservableList.parallelStream().forEach(matrix -> {
                    analyzerDataObservableList.add(analyzerModule.gatherData(matrix));
                });
                return null;
            }
        }).start();
        return analyzerDataObservableList;
    }
}
