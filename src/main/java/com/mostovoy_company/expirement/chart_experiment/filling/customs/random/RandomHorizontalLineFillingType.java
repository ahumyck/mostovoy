package com.mostovoy_company.expirement.chart_experiment.filling.customs.random;

import org.springframework.stereotype.Component;

@Component
public class RandomHorizontalLineFillingType extends RandomLineFillingType {
    public RandomHorizontalLineFillingType() {
        super("Горизонтальные линии с отступами");
    }

    @Override
    protected void set(int first, int second, int value) {
        this.matrix[first][second] = value;
    }
}
