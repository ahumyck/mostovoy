package com.mostovoy_company;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.var;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("ChartConfigurationTab.fxml")
public class ChartConfigurationTab {

    private static final double errorCorrection = 0.0001;

    @FXML
    public TextField startProbabilityField;
    @FXML
    public TextField finalProbabilityField;
    @FXML
    public AnchorPane chartConfigurationTab;

    private double startProbability = 0.0;
    private double finalProbability = 1.0 ;
    private boolean changed = false;

    public boolean isChanged() {
        var old = changed;
        changed = false;
        return old;
    }

    public double getStartProbability() {
        return startProbability - errorCorrection;
    }

    public double getFinalProbability() {
        return finalProbability + errorCorrection;
    }

    public void initialize(){
        startProbabilityField.setText(String.valueOf(startProbability));
        finalProbabilityField.setText(String.valueOf(finalProbability));
    }

    public Node getContent(){
        return this.chartConfigurationTab;
    }

    public void saveConfiguration(){
        try {
            var newStartProbability = Double.parseDouble(startProbabilityField.getText());
            var newFinalProbability = Double.parseDouble(finalProbabilityField.getText());
            if(newStartProbability != startProbability || newFinalProbability != finalProbability){
                changed = true;
                startProbability = newStartProbability;
                finalProbability = newFinalProbability;
            }
        }
        catch (Exception e){
            startProbability = 0.0;
            finalProbability = 1.0;
            startProbabilityField.setText(String.valueOf(startProbability));
            finalProbabilityField.setText(String.valueOf(finalProbability));
        }
    }
}
