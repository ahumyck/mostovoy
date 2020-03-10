package company.filling;

public abstract class CustomTestFillingType extends FillingTypeV2 {

    public CustomTestFillingType(String name) {
        super(name);
    }

    @Override
    abstract public int[][] getMatrix();
}
