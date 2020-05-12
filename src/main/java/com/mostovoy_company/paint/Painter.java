package com.mostovoy_company.paint;

import com.mostovoy_company.expirement.chart_experiment.entity.Cell;
import com.mostovoy_company.expirement.chart_experiment.entity.Matrix;
import com.mostovoy_company.expirement.chart_experiment.programminPercolation.percolation.PercolationRelation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Painter {

    private ColorRepository colorRepository;

    public Painter(@Qualifier("colorRandomRepository") ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    private void paintClusterMatrix(double size, Matrix matrix, GraphicsContext graphicsContext2D) {
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getRandomColorForCluster(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, size, size);
        });
    }

    private void drawSplitterMatrixLines(double size, Matrix matrix, GraphicsContext graphicsContext2D) {
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getY() * size + size - 0.5, cell.getX() * size, 0.5, size);
        });
    }

    private void paintMatrix(double size, Matrix matrix, GraphicsContext graphicsContext2D) {
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getColorForCell(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, size, size);
        });
    }

    private void drawRelationLines(double size, List<PercolationRelation> relations, GraphicsContext graphicsContext2D) {
        relations.forEach(relation -> {
            graphicsContext2D.moveTo(relation.getRedCell().getY() * size + size/2, relation.getRedCell().getX() * size + size/2);
            graphicsContext2D.lineTo(relation.getDarkRedCell().getY() * size + size / 2, relation.getDarkRedCell().getX() * size + size / 2);
        });
        graphicsContext2D.stroke();
    }

    private void paintPath(double size, List<Cell> path, GraphicsContext graphicsContext2D) {
        path.forEach(cell -> {
            graphicsContext2D.setFill(cell.hasClusterMark() ? Color.DARKRED : Color.RED);
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, size, size);
        });
    }

    private void paintRelations(double size, List<PercolationRelation> relations, GraphicsContext graphicsContext2D) {
        relations.stream().map(PercolationRelation::getDarkRedCell).forEach(cell -> {
            graphicsContext2D.setFill(Color.GREEN);
            graphicsContext2D.fillRect(cell.getY() * size, cell.getX() * size, size, size);
        });
    }

    public void paintCanvas(Canvas grid, Matrix matrix, double gridSize) {
        double size = gridSize / (matrix.getSize() - 2 * Matrix.OFFSET);
        grid.setWidth(gridSize);
        grid.setHeight(gridSize);
        GraphicsContext graphicsContext2D = grid.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fill();

        paintClusterMatrix(size, matrix, graphicsContext2D);
        drawSplitterMatrixLines(size, matrix, graphicsContext2D);
    }


    public void paintLightningBoltAndRelations(StackPane pane, List<Cell> path, List<PercolationRelation> relations, Matrix matrix, double gridSize) {
        double size = gridSize / (matrix.getSize() - 2 * Matrix.OFFSET);
        Canvas canvas = new Canvas();
        canvas.setHeight(gridSize);
        canvas.setWidth(gridSize);
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, pane.getWidth(), pane.getHeight());

        paintMatrix(size, matrix, graphicsContext2D);
        paintPath(size, path, graphicsContext2D);
        paintRelations(size, relations, graphicsContext2D);
        drawRelationLines(size, relations, graphicsContext2D);
        drawSplitterMatrixLines(size, matrix, graphicsContext2D);

        pane.getChildren().clear();
        pane.getChildren().add(canvas);
    }
}
