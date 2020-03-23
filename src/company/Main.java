package company;

import company.entity.Matrix;
import company.filling.FillingType;
import company.filling.RandomFillingType;
import company.filling.customs.LineFillingType;
import company.filling.customs.MaltTestFillingType;
import company.filling.customs.SquareFillingType;
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
        launch(args);
    }
}
