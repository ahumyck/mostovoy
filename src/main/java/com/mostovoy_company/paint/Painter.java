package com.mostovoy_company.paint;

import com.mostovoy_company.entity.Cell;
import com.mostovoy_company.entity.Matrix;
import com.mostovoy_company.programminPercolation.PercolationRelation;
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

import java.util.List;

public class Painter {

    private static final int SIZE = 15;

    private ColorGradientRepository colorRepository = new ColorGradientRepository();

    public void paintCanvas(AnchorPane grid, Matrix matrix) {
        double size = grid.getHeight() / (matrix.getSize() - 2);
        Canvas canvas = new Canvas(grid.getWidth(), grid.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, grid.getWidth(), grid.getHeight());
        matrix.stream().filter(Cell::isNotEmpty).forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getRandomColorForCluster(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size , size, size );
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
        grid.getChildren().clear();
        grid.getChildren().add(canvas);
    }

    public void paintLineChart(AnchorPane pane, List<LineChartNode> nodes, String title) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setPrefHeight(pane.getPrefHeight());
        numberLineChart.setPrefWidth(pane.getPrefWidth());
        numberLineChart.setTitle(title);
        XYChart.Series series = new XYChart.Series();
        series.setName(title);
//        numberLineChart.getStylesheets().add(".chart-line-symbol {}");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        nodes.forEach(node -> datas.add(new XYChart.Data(node.x, node.y)));
        series.setData(datas);
        numberLineChart.getData().add(series);
        pane.getChildren().clear();
        pane.getChildren().add(numberLineChart);
    }

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

    public void paintLightningBoltCanvas(AnchorPane pane, List<Cell> path, List<PercolationRelation> relations, Matrix matrix)
    {
        double size = pane.getHeight()/ (matrix.getSize() - 2);
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, pane.getWidth(), pane.getHeight());
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getColorForCell(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
        path.forEach(cell -> {
            graphicsContext2D.setFill(cell.hasClusterMark() ? Color.GREEN : Color.RED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
        relations.stream().map(PercolationRelation::getBlackCell).forEach(cell -> {
            graphicsContext2D.setFill(Color.DARKRED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
        //line drawing section
//        relations.stream().forEach(relation -> {
//            graphicsContext2D.moveTo(relation.getBlackCell().getX()* size + size/2, relation.getBlackCell().getY()* size+ size/2);
//            graphicsContext2D.lineTo(relation.getRedCell().getX()* size+ size/2, relation.getRedCell().getY()* size+ size/2);
//            graphicsContext2D.stroke();
//        });
        pane.getChildren().clear();
        pane.getChildren().add(canvas);

    }

//    public void paintPercolationProgramming(AnchorPane pane, List<PercolationRelation> relations, Matrix matrix)
//    {
//        double size = pane.getHeight()/ (matrix.getSize() - 2);
//        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
//        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
//        graphicsContext2D.setFill(Color.WHITE);
//        graphicsContext2D.fillRect(0, 0, pane.getWidth(), pane.getHeight());
//        matrix.stream().forEach(cell -> {
//            graphicsContext2D.setFill(colorRepository.getColorForCell(cell.getClusterMark()));
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
//            graphicsContext2D.setFill(Color.GRAY);
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
//            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
//            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
//        });
//        relations.stream().map(PercolationRelation::getBlackCell).forEach(cell -> {
//            graphicsContext2D.setFill(Color.DARKRED);
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
//            graphicsContext2D.setFill(Color.GRAY);
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
//            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
//            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
//            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
//        });
//        pane.getChildren().clear();
//        pane.getChildren().add(canvas);
//
//    }
}
