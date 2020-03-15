package company;

import company.entity.Matrix;
import company.filling.RandomFillingType;
import company.lightning.LightningBolt;
import company.lightning.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Percolation");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
//        long l = System.currentTimeMillis();
//        RandomFillingType randomFillingType = new RandomFillingType();
//        randomFillingType.setPercolationProbability(0.5);
//        randomFillingType.setSize(30);
//        Matrix matrix = new Matrix(randomFillingType);
//        Pair<List<Pair<Integer, Integer>>, Integer> shortestWays = LightningBolt.findShortestWay(matrix);
//        long l1 = System.currentTimeMillis();
//        System.out.println(matrix);
//        System.out.println();
//        System.out.println("shortest way: " + shortestWays.getX() + " = " + shortestWays.getY());
//        System.out.println("Total time: " + (l1 - l) + " ms");
        launch(args);
    }
}
