package com.mostovoy_company.expirement.filling.customs;

import com.mostovoy_company.expirement.filling.FillingType;

public abstract class CustomTestFillingType extends FillingType {
    public CustomTestFillingType(String name) {
        super(name);
    }

    @Override
    abstract public int[][] getMatrix();
}
