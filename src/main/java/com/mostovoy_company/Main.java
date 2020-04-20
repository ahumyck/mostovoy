package com.mostovoy_company;

import com.mostovoy_company.controllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    public static Stage pStage;

    @Override
    public void start(Stage primaryStage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MainController.class);
        primaryStage.setTitle("Исследование перколяции");
        primaryStage.setScene(new Scene(root));
        pStage = primaryStage;
        primaryStage.show();
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(SpringBootExampleApplication.class)
                .run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}