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

    public ObservableList<TableViewAnalyzerData> initializeAnalyzerExperiments(int number, int matrixSize, double probability) {
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setSize(matrixSize);
        fillingType.setPercolationProbability(probability);
        ObservableList<TableViewAnalyzerData> analyzerDataObservableList = FXCollections.observableArrayList();

        for (int i = 0; i < number; i++)
            analyzerDataObservableList.add(new TableViewAnalyzerData(analyzerModule.gatherData(new Matrix(fillingType),probability)));


        return analyzerDataObservableList;
    }
}
