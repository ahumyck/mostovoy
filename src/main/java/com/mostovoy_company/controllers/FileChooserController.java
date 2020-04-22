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


    public Optional<List<File>> getMultipleFiles(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузка файлов");
        List<File> files = chooser.showOpenMultipleDialog(Main.pStage);
        if(files == null) return Optional.empty();
        else return Optional.of(files);
    }

    public Optional<File> getSingleFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузка файлов");
        File file = chooser.showOpenDialog(Main.pStage);
        if(file == null) return Optional.empty();
        else return Optional.of(file);
    }

    public Optional<File> getFileToSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранение файла");
        File file = chooser.showSaveDialog(Main.pStage);
        if (file != null) return Optional.of(file);
        else return Optional.empty();
    }
}
