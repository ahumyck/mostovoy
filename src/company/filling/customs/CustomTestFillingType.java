package company.filling.customs;

import company.filling.FillingType;

public abstract class CustomTestFillingType extends FillingType {
    public CustomTestFillingType(String name) {
        super(name);
    }

    @Override
    abstract public int[][] getMatrix();
}
