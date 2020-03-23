package company.filling.customs;

public class LineFillingType extends CustomTestFillingType {

    private final int MATRIX_SIZE = 50;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public LineFillingType(String name) {
        super(name);
    }

    @Override
    public int[][] getMatrix() {
        int step = 2;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if(i % step == 0) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrix[j][i] = 1;
                }
            }
        }
        return matrix;
    }
}
