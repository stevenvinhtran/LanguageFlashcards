package com.stevenvinhtran.languageflashcards.Model;

import com.stevenvinhtran.languageflashcards.Controller.BrowserController;
import com.stevenvinhtran.languageflashcards.Controller.EditFlashcardController;
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

        if (fxml == "browser-view.fxml") {
            BrowserController browserController = fxmlLoader.getController();
            browserController.loadFlashcardTable();
        }
    }

    public SceneSwitcher(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setScene(scene);
        stage.show();

        if (fxml == "browser-view.fxml") {
            BrowserController browserController = fxmlLoader.getController();
            browserController.loadFlashcardTable();
        }
    }

    // Switch scene to Edit Flashcard
    public SceneSwitcher(Flashcard flashcard) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource("edit-flashcard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setTitle("Edit Flashcard");
        stage.setScene(scene);
        stage.show();
        EditFlashcardController editFlashcardController = fxmlLoader.getController();
        editFlashcardController.setFlashcard(flashcard);
    }

}
