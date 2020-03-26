package company.paint;

import company.entity.Cell;
import company.entity.Matrix;
import company.lightning.Pair;
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
//        numberLineChart.getStylesheets().add(".chart-line-symbol {}");
        ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
        nodes.forEach(node -> data.add(new XYChart.Data(node.x, node.y)));
        series.setData(data);
        chart.getData().add(series);
    }

    public void paintLightningBoltCanvas(AnchorPane pane, Pair<List<Pair<Integer,Integer>>,Integer> path, Matrix matrix)
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
        path.getFirst().forEach(dot -> {
            Cell cell = matrix.getCell(dot.getFirst() + Matrix.OFFSET, dot.getSecond()+ Matrix.OFFSET);
            graphicsContext2D.setFill(cell.hasClusterMark() ? Color.GREEN : Color.RED);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, size);
            graphicsContext2D.setFill(Color.GRAY);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size, cell.getY() * size, 0.5, size);
            graphicsContext2D.fillRect(cell.getX() * size , cell.getY() * size + size - 0.5, size, 0.5);
            graphicsContext2D.fillRect(cell.getX() * size + size - 0.5, cell.getY() * size , 0.5, size);
        });
        pane.getChildren().clear();
        pane.getChildren().add(canvas);

    }
}
