package com.mostovoy_company;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("ChartConfigurationTab.fxml")
public class ChartConfigurationTab {

    @FXML
    public TextField startProbabilityField;
    @FXML
    public TextField stepProbabilityField;
    @FXML
    public TextField finalProbabilityField;

    public AnchorPane chartConfigurationTab;


    @Getter
    private double startProbability;
    @Getter
    private double stepProbability;
    @Getter
    private double finalProbability;


    public Node getContent(){
        return this.chartConfigurationTab;
    }

}
