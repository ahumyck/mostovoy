package company.filling;

abstract public class FillingTypeV2 {
    private String name;

    public FillingTypeV2(String name) {
        this.name = name;
    }

    abstract public int[][] getMatrix();

    @Override
    public String toString() {
        return name;
    }
}
