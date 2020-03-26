package company.filling.customs;

public class HorizontalLineFillingType extends CustomTestFillingType {

    private final int MATRIX_SIZE = 30;
    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public HorizontalLineFillingType() {
        super("Горизонтальные линии");
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
