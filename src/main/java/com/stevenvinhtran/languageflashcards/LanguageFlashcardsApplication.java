package com.stevenvinhtran.languageflashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageFlashcardsApplication extends Application {
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LanguageFlashcardsApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        LanguageFlashcardsApplication.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}