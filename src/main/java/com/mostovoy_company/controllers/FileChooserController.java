package com.mostovoy_company.controllers;

import com.mostovoy_company.Main;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
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

    public static FileChooser.ExtensionFilter jsonExtensionFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
    public static FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");

    @FXML
    public void initialize(){
    }

    private FileChooser makeFileChooser(String title, FileChooser.ExtensionFilter filter){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(filter);
        chooser.setTitle(title);
        return chooser;
    }

    public Optional<List<File>> getMultipleFiles(FileChooser.ExtensionFilter filter){
        FileChooser chooser = makeFileChooser("Загрузка файлов", filter);
        List<File> files = chooser.showOpenMultipleDialog(Main.pStage);
        if(files == null) return Optional.empty();
        else return Optional.of(files);
    }

    public Optional<File> getSingleFile(FileChooser.ExtensionFilter filter){
        FileChooser chooser = makeFileChooser("Загрузка файла", filter);
        File file = chooser.showOpenDialog(Main.pStage);
        if(file == null) return Optional.empty();
        else return Optional.of(file);
    }

    public Optional<File> getFileToSave(FileChooser.ExtensionFilter filter) {
        FileChooser chooser = makeFileChooser("Сохранение файла", filter);
        File file = chooser.showSaveDialog(Main.pStage);
        if (file != null) return Optional.of(file);
        else return Optional.empty();
    }
}
