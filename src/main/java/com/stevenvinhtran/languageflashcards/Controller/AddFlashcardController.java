package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class AddFlashcardController {
    private Flashcard flashcard;
    private LocalDateTime now = LocalDateTime.now();

    @FXML
    private Button backButton;
    @FXML
    private Label definitionLabel;
    @FXML
    private TextArea definitionTextArea;
    @FXML
    private AnchorPane addFlashcardAnchorPane;
    @FXML
    private Button createFlashcardButton;
    @FXML
    private Label termLabel;
    @FXML
    private TextField termTextField;
    @FXML
    private Label typeLabel;
    @FXML private MenuButton typeMenuButton;
    @FXML private MenuItem vocabularyMenuItem;
    @FXML private MenuItem grammarMenuItem;

    @FXML
    void onVocabularyItemSelect(ActionEvent event) {
        typeMenuButton.setText(vocabularyMenuItem.getText());
    }

    @FXML
    void onGrammarItemSelect(ActionEvent event) {
        typeMenuButton.setText(grammarMenuItem.getText());
    }

    @FXML
    void onCreateFlashcardClick(ActionEvent event) throws IOException {
        String term = termTextField.getText();
        String definition = definitionTextArea.getText();
        String type = typeMenuButton.getText();
        LocalDateTime reviewDate = now;

        if (term.isBlank() && definition.isBlank()) {
            JOptionPane.showMessageDialog(null, "Term and Definition are blank", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (term.isBlank()) {
            JOptionPane.showMessageDialog(null, "Term field is blank", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (definition.isBlank()) {
            JOptionPane.showMessageDialog(null, "Definition area is blank", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Initialize SM-2 SRS parameters
            flashcard = new Flashcard(
                    term, definition, type,
                    reviewDate, now, // dateAdded is now
                    0, 2.5, 1 // repetitions, easeFactor, interval
            );

            CSVProcessor.addFlashcard(flashcard);
            new SceneSwitcher("browser-view.fxml", "Browser");
        }
    }

    @FXML
    void returnToBrowserScene(ActionEvent event) throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }
}