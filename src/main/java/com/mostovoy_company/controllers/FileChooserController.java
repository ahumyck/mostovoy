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

    private static String lastUsedDirectory = "NULL";

    @FXML
    public void initialize() {
    }

    private FileChooser makeFileChooser(String title, FileChooser.ExtensionFilter... filters) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(filters);
        chooser.setTitle(title);
        if (!lastUsedDirectory.equals("NULL"))
            chooser.setInitialDirectory(new File(lastUsedDirectory));
        return chooser;
    }

    private Optional<List<File>> handle(List<File> files) {
        if (files == null) return Optional.empty();
        else {
            updateLastUsedDirectory(files.get(0));
            return Optional.of(files);
        }
    }

    private Optional<File> handle(File file) {
        if (file == null) return Optional.empty();
        else {
            updateLastUsedDirectory(file);
            return Optional.of(file);
        }
    }

    private void updateLastUsedDirectory(File file) {
        lastUsedDirectory = file.getPath().substring(0, file.getPath().length() - file.getName().length());
    }

    public Optional<List<File>> getMultipleFiles(FileChooser.ExtensionFilter... filters) {
        return handle(makeFileChooser("Загрузка файлов", filters).showOpenMultipleDialog(Main.pStage));
    }

    public Optional<File> getSingleFile(FileChooser.ExtensionFilter... filters) {
        return handle(makeFileChooser("Загрузка файла", filters).showOpenDialog(Main.pStage));
    }

    public Optional<File> getFileToSave(FileChooser.ExtensionFilter... filters) {
        return handle(makeFileChooser("Сохранение файла", filters).showSaveDialog(Main.pStage));
    }
}
