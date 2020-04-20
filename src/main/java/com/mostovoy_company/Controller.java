package com.mostovoy_company;

import com.mostovoy_company.chart.ChartsController;
import com.mostovoy_company.table_view.AnalyzerController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


@Component
@FxmlView("sample.fxml")
@Slf4j
public class Controller {

    @FXML
    public Tab fullExperiment;

    private final FxWeaver fxWeaver;

    @FXML
    public Tab manualMatrixTab;

    @FXML
    public Tab analyzerTable;


    public Controller(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        analyzerTable.setContent(fxWeaver.loadController(AnalyzerController.class).getContent());
        fullExperiment.setContent(fxWeaver.loadController(ChartsController.class).getContent());
        manualMatrixTab.setContent(fxWeaver.loadController(ManualMatrixController.class).getContent());

    }
}