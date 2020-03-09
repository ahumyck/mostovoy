package company.filling;

import company.entity.Matrix;

abstract public class FillingType {

    private String name;

    public FillingType(String name) {
        this.name = name;
    }

    abstract public void fillMatrix(Matrix matrix);

    @Override
    public String toString() {
        return name;
    }
}
