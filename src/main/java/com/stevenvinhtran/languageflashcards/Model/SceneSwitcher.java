package com.stevenvinhtran.languageflashcards.Model;

import com.stevenvinhtran.languageflashcards.LanguageFlashcardsApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    Stage stage = LanguageFlashcardsApplication.getStage();

    public SceneSwitcher(String fxml, String stageTitle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
    }

    public SceneSwitcher(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setScene(scene);
        stage.show();
    }

}
