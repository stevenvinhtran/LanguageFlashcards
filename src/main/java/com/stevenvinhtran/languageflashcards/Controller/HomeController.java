package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {

    @FXML
    void goToBrowserScreen() throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void goToGrammarScene() throws IOException {
        new SceneSwitcher("grammar-view.fxml", "Grammar");
    }

    @FXML
    void goToVocabularyScene() throws IOException {
        new SceneSwitcher("vocabulary-view.fxml", "Vocabulary");
    }
}
