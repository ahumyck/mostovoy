package company.paint;

import company.entity.Matrix;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Painter {

    private static final int SIZE = 15;

    private ColorRepository colorRepository = new ColorRepository();

    public void paintCanvas(AnchorPane grid, Matrix matrix) {
        Double size = Double.min(grid.getWidth(), grid.getHeight())/matrix.getSize();
        Canvas canvas = new Canvas(grid.getWidth(), grid.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, grid.getWidth(), grid.getHeight());
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getColorForCluster(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
        grid.getChildren().add(canvas);
    }
}
