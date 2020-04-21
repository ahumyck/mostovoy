package com.mostovoy_company.expirement.filling;

abstract public class FillingType {
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
