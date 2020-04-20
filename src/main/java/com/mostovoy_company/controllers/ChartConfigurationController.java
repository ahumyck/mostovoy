package com.mostovoy_company.controllers;

import com.mostovoy_company.Main;
import com.mostovoy_company.chart.LineChartData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("ConfigurationWindow.fxml")
@Scope("prototype")
public class ChartConfigurationController {

    @FXML
    public TabPane chartsConfigurationsTabPane;
    @FXML
    public AnchorPane chartConfigurationTabPane;

    @FXML
    public void initialize(){
    }

    public void show(List<LineChartData> charts){
        charts.forEach(chart ->{
            Tab tab = new Tab();
            tab.setContent(chart.getConfigurationTab());
            tab.setText(chart.getTabName());
            chartsConfigurationsTabPane.getTabs().add(tab);
        });
        Stage stage = new Stage();
        stage.setTitle("Конфигурация графиков");
        stage.setScene(new Scene(chartConfigurationTabPane));
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.pStage);
        stage.setMaxWidth(500.0);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            charts.forEach(chartData -> Platform.runLater(chartData::saveConfiguration));
        });
    }


}
