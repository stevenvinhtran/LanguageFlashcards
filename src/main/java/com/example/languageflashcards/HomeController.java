package com.example.languageflashcards;

import javafx.fxml.FXML;

public class HomeController {
    @FXML
    protected void onEditTermsButtonClick() {
        System.out.println("Go to Edit Terms screen");
    }

    @FXML
    protected void onStudyVocabularyButtonClick() {
        System.out.println("Go to Study Vocab screen");
    }

    @FXML
    protected void onStudyGrammarButtonClick() {
        System.out.println("Go to Study Grammar screen");
    }
}