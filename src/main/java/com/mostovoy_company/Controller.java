package com.mostovoy_company;

import com.mostovoy_company.chart.ChartsController;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.FillingType;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.filling.customs.*;
import com.mostovoy_company.paint.Painter;
import com.mostovoy_company.services.MainService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.DISCRETE;
import static com.mostovoy_company.programminPercolation.distance.DistanceCalculatorTypeResolver.PYTHAGORAS;

//import com.mostovoy_company.stat.NormalizedStatManager;

@Component
@FxmlView("sample.fxml")
@Slf4j
public class Controller {

    public Tab fullExperiment;

    private final FxWeaver fxWeaver;

    @FXML
    public Tab manualMatrixTab;
    @FXML
    public TextField matrixCountAnalyzer;
    @FXML
    public TextField concentrationAnalyzer;
    @FXML
    public TextField matrixSizeAnalyzer;
    @FXML
    public Button applyAnalyzerExperiment;
    @FXML
    public TableView analyzerTable;


    public Controller(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
//        tapeCheckBox.setSelected(false);
        fullExperiment.setContent(fxWeaver.loadController(ChartsController.class).getStatisticCharts());
        manualMatrixTab.setContent(fxWeaver.loadController(ManualMatrixController.class).getContent());
//        applyAnalyzerExperiment.setOnAction(event -> {
//            Map<Integer, Integer> map = new LinkedHashMap<>();
//            List<Integer> sizes = Arrays.stream(this.matrixSizeAnalyzer.getText().split(",")).map(Integer::valueOf).collect(Collectors.toList());
//            log.info("=> init: " + map);
//        });
    }


    int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}