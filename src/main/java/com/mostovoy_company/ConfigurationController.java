package com.mostovoy_company;

import com.mostovoy_company.chart.LineChartData;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("ConfigurationWindow.fxml")
public class ConfigurationController {

    @FXML
    public TabPane chartsConfigurations;

    private List<LineChartData> charts;

    public ConfigurationController(List<LineChartData> charts){
        this.charts = charts;
    }

    @FXML
    public void initialize(){
        charts.forEach(chart ->{
            Tab tab = new Tab();
            tab.setContent(chart.getConfigurationTab());
            tab.setText(chart.getTabName());
            chartsConfigurations.getTabs().add(tab);
        });
    }


}
