package com.mostovoy_company.expirement.chart_experiment.filling.customs.random;

import org.springframework.stereotype.Component;

@Component
public class RandomVerticalLineFillingType extends RandomLineFillingType {
    public RandomVerticalLineFillingType() {
        super("Вертикальные линии с отступами");
    }

    @Override
    protected void set(int first, int second, int value) {
        this.matrix[second][first] = value;
    }
}
