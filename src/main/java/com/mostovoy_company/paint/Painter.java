package com.mostovoy_company.paint;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.programminPercolation.percolation.PercolationRelation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Painter {

    private ColorGradientRepository colorRepository = new ColorGradientRepository();

    public LineChart<Number, Number> paintEmptyLineChart(AnchorPane pane, String title)
    {
        pane.getChildren().clear();
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setPrefHeight(pane.getPrefHeight());
        numberLineChart.setPrefWidth(pane.getPrefWidth());
        numberLineChart.setTitle(title);
        pane.getChildren().clear();
        pane.getChildren().add(numberLineChart);
        return numberLineChart;
    }

    public void addSeriesToLineChart(LineChart<Number, Number> chart, String title, List<LineChartNode> nodes)
    {
        XYChart.Series series = new XYChart.Series();
        series.setName(title);
//        chart.getStylesheets().add(".chart-line-symbol {}");
        ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
        nodes.forEach(node -> {
            XYChart.Data dot = new XYChart.Data(node.x, node.y);
            Rectangle rect = new Rectangle(0, 0);
            rect.setVisible(false);
            dot.setNode(rect);
            data.add(dot);
        });
        series.setData(data);
        chart.getData().add(series);
    }

    public void addObservableSeries(LineChart<Number, Number> chart, String title, ObservableList<XYChart.Data> data){
        XYChart.Series series = new XYChart.Series();
        series.setName(title);
        series.setData(data);
        chart.getData().add(series);
    }

    private void paintClusterMatrix(double size, Matrix matrix, GraphicsContext graphicsContext2D){
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getRandomColorForCluster(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    private void drawLines(double size, Matrix matrix, GraphicsContext graphicsContext2D){
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
    }

    private void paintMatrix(double size, Matrix matrix, GraphicsContext graphicsContext2D){
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getColorForCell(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    private void paintPath(double size, List<Cell> path, GraphicsContext graphicsContext2D){
        path.forEach(cell -> {
            graphicsContext2D.setFill(cell.hasClusterMark() ? Color.GREEN : Color.RED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    private void paintTape(double size, List<Cell> tape, GraphicsContext graphicsContext2D){
        tape.forEach(cell -> {
            graphicsContext2D.setFill(Color.BLUE);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    private void paintRelations(double size, List<PercolationRelation> relations, GraphicsContext graphicsContext2D){
        relations.stream().map(PercolationRelation::getDarkRedCell).forEach(cell -> {
            graphicsContext2D.setFill(Color.DARKRED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    private void paintRelationOnTape(double size, List<PercolationRelation> relations, List<Cell> tape, GraphicsContext graphicsContext2D){
        List<Cell> darkRedCells = relations.stream().map(PercolationRelation::getDarkRedCell).collect(Collectors.toList());
        tape.stream().filter(darkRedCells::contains).forEach(cell -> {
            graphicsContext2D.setFill(Color.DARKRED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
        });
    }

    public void paintCanvas(AnchorPane grid, Matrix matrix) {
        double size = grid.getHeight() / (matrix.getSize() - 2 * Matrix.OFFSET);
        Canvas canvas = new Canvas(grid.getWidth(), grid.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, grid.getWidth(), grid.getHeight());

        paintClusterMatrix(size, matrix, graphicsContext2D);
        drawLines(size, matrix, graphicsContext2D);

        grid.getChildren().clear();
        grid.getChildren().add(canvas);
    }

    public void paintLightningBoltAndTape(AnchorPane pane, List<Cell> path, List<Cell> tape,List<PercolationRelation> relations, Matrix matrix){
        double size = pane.getHeight()/ (matrix.getSize() - 2 * Matrix.OFFSET);
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, pane.getWidth(), pane.getHeight());

        paintMatrix(size, matrix, graphicsContext2D);
        paintPath(size, path, graphicsContext2D);
        paintTape(size, tape, graphicsContext2D);
        paintRelationOnTape(size, relations, tape, graphicsContext2D);
        drawLines(size, matrix, graphicsContext2D);

        pane.getChildren().clear();
        pane.getChildren().add(canvas);
    }

    public void paintLightningBoltAndRelations(AnchorPane pane, List<Cell> path, List<PercolationRelation> relations, Matrix matrix){
        double size = pane.getHeight()/ (matrix.getSize() - 2 * Matrix.OFFSET);
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, pane.getWidth(), pane.getHeight());

        paintMatrix(size, matrix, graphicsContext2D);
        paintPath(size,path, graphicsContext2D);
        paintRelations(size,relations,graphicsContext2D);
        drawLines(size, matrix, graphicsContext2D);

        pane.getChildren().clear();
        pane.getChildren().add(canvas);
    }
}
