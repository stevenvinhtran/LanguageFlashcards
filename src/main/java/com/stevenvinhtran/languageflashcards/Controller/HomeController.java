package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button editFlashcardsButton;

    @FXML
    private AnchorPane homeAnchorPane;

    @FXML
    private Button studyGrammarButton;

    @FXML
    private Button studyVocabularyButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void goToBrowserScreen() throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void goToGrammarScene() {

    }

    @FXML
    void goToVocabularyScene() {

    }

}
