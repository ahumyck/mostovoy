package company.filling;

abstract public class FillingType {
    private String name;

    public FillingType(String name) {
        this.name = name;
    }

    abstract public int[][] getMatrix();

    @Override
    public String toString() {
        return name;
    }
}
