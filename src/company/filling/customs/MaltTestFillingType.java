package company.filling.customs;

public class MaltTestFillingType extends CustomTestFillingType {

    private static final int[][] MATRIX = {
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {0, 0, 0, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0}};

    public MaltTestFillingType(String name) {
        super(name);
    }

    @Override
    public int[][] getMatrix() {
        return MATRIX;
    }
}