package com.mostovoy_company.expirement.table_experiment.analyzer;

import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.filling.RandomFillingType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyzerManager {

    private final AnalyzerModule analyzerModule;

    public AnalyzerManager(AnalyzerModule analyzerModule) {
        this.analyzerModule = analyzerModule;
    }

    public AnalyzerDataRepository initializeAnalyzerExperiments(int number, int matrixSize, double probability) {
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setSize(matrixSize);
        fillingType.setPercolationProbability(probability);
        List<AnalyzerData> dataArrayList = new ArrayList<>();

        for (int i = 0; i < number; i++)
            dataArrayList.add(analyzerModule.gatherData(new Matrix(fillingType),probability));


        return new AnalyzerDataRepository(dataArrayList);
    }
}
