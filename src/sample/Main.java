package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.matrix.Matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
        Matrix matrix = new Matrix(15);
        Map<Integer,Matrix> map = new HashMap<>();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 100 ; i++) {
            map.put(i,new Matrix(100).generateValues(0.5).joinClusters());
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1 - l);
        //launch(args);
    }
}
