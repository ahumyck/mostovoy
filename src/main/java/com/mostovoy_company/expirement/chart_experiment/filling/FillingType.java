package com.mostovoy_company.expirement.chart_experiment.filling;

public abstract class FillingType {
    private String name;

    public FillingType(String name) {
        this.name = name;
    }

    abstract public int[][] getMatrix();

    abstract public String getName();

    @Override
    public String toString() {
        return name;
    }
}
