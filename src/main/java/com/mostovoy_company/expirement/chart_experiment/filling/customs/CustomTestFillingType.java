package com.mostovoy_company.expirement.chart_experiment.filling.customs;

import com.mostovoy_company.expirement.chart_experiment.filling.FillingType;

public abstract class CustomTestFillingType extends FillingType {
    public CustomTestFillingType(String name) {
        super(name);
    }

    @Override
    abstract public int[][] getMatrix();

    @Override
    public String getName() {
        return toString();
    }
}
