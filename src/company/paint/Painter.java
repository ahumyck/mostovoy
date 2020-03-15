package company.paint;

import company.entity.Matrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.List;

public class Painter {

    private static final int SIZE = 15;

    private ColorRepository colorRepository = new ColorRepository();

    public void paintCanvas(AnchorPane grid, Matrix matrix) {
        double size = Double.min(grid.getWidth(), grid.getHeight()) / matrix.getSize();
        Canvas canvas = new Canvas(grid.getWidth(), grid.getHeight());
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.WHITE);
        graphicsContext2D.fillRect(0, 0, grid.getWidth(), grid.getHeight());
        matrix.stream().forEach(cell -> {
            graphicsContext2D.setFill(colorRepository.getColorForCluster(cell.getClusterMark()));
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
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
}
