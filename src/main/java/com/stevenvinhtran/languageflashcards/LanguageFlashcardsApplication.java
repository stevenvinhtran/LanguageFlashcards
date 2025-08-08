package com.stevenvinhtran.languageflashcards;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.SpacedRepetition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LanguageFlashcardsApplication extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setWidth(976);
        stage.setHeight(759);
        stage.setMinWidth(976);
        stage.setMinHeight(759);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        LanguageFlashcardsApplication.stage = stage;

        ArrayList<String> settings = CSVProcessor.loadSettings();
        int newVocabCardsPerDay = Integer.parseInt(settings.get(0));
        int newGrammarCardsPerDay = Integer.parseInt(settings.get(1));
        SpacedRepetition.updateSettings(newVocabCardsPerDay, newGrammarCardsPerDay, settings.get(2), settings.get(3));
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}