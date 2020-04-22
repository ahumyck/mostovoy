package com.mostovoy_company.controllers;

import com.mostovoy_company.Main;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
@FxmlView
public class FileChooserController {

    @FXML
    public void initialize(){
    }

    private Stage initializeStage(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Main.pStage);
        //todo: убрать эту хрень
        stage.setMaxHeight(1);
        stage.setMaxWidth(1);
        return stage;
    }


    public Optional<List<File>> getMultipleFiles(){
        Stage stage = initializeStage();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузка файлов");
        stage.show();
        List<File> files = chooser.showOpenMultipleDialog(stage);
        stage.close();
        if(files == null) return Optional.empty();
        else return Optional.of(files);
    }

    public Optional<File> getSingleFile(){
        Stage stage = initializeStage();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузка файлов");
        stage.show();
        File file = chooser.showOpenDialog(stage);
        stage.close();
        if(file == null) return Optional.empty();
        else return Optional.of(file);
    }

    public Optional<String> getPossibleFilePathToSave() {
        Stage stage = initializeStage();
        FileChooser chooser = new FileChooser();
        stage.show();
        chooser.setTitle("Сохранение файла");
        File file = chooser.showSaveDialog(stage);
        stage.close();
        if (file != null) return Optional.of(file.getPath());
        else return Optional.empty();
    }
}
