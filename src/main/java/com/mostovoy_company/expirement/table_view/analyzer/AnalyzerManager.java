package com.mostovoy_company.expirement.table_view.analyzer;

import com.mostovoy_company.expirement.entity.Matrix;
import com.mostovoy_company.expirement.filling.RandomFillingType;
import com.mostovoy_company.expirement.table_view.TableViewAnalyzerData;
import com.mostovoy_company.expirement.table_view.TableViewAnalyzerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyzerManager {

    @Autowired
    private AnalyzerModule analyzerModule;

    public TableViewAnalyzerDataRepository initializeAnalyzerExperiments(int number, int matrixSize, double probability) {
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setSize(matrixSize);
        fillingType.setPercolationProbability(probability);
        List<TableViewAnalyzerData> analyzerDataObservableList = new ArrayList<>();

        for (int i = 0; i < number; i++)
            analyzerDataObservableList.add(new TableViewAnalyzerData(analyzerModule.gatherData(new Matrix(fillingType),probability)));


        return new TableViewAnalyzerDataRepository(analyzerDataObservableList);
    }
}
