package com.mostovoy_company.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


@Component
@FxmlView("main.fxml")
@Slf4j
public class MainController {
    @FXML
    public Tab fullExperiment;
    private final FxWeaver fxWeaver;
    @FXML
    public Tab manualMatrixTab;
    @FXML
    public Tab analyzerTable;

    public MainController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        analyzerTable.setContent(fxWeaver.loadController(AnalyzerController.class).getContent());
        fullExperiment.setContent(fxWeaver.loadController(ChartsController.class).getContent());
        manualMatrixTab.setContent(fxWeaver.loadController(ManualMatrixController.class).getContent());
    }
}