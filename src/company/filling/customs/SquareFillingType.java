package company.filling.customs;

public class SquareFillingType extends CustomTestFillingType  {

    private final int MATRIX_SIZE = 50;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public SquareFillingType(String name) {
        super(name);
    }

    @Override
    public int[][] getMatrix() {
        int step = 2;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if(i % step == 0) {
                for (int j = i; j < MATRIX_SIZE - i; j++) {
                    matrix[i][j] = 1;
                    matrix[j][i] = 1;
                    matrix[MATRIX_SIZE - i - 1][j] = 1;
                    matrix[MATRIX_SIZE - i - 1][MATRIX_SIZE - j - 1] = 1;

                }
            }
        }
        return matrix;
    }
}
