package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVLoader;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditFlashcardController {
    private Flashcard flashcard;
    private Flashcard oldFlashcard = new Flashcard("","","","");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private Button backButton;

    @FXML
    private Label definitionLabel;

    @FXML
    private TextArea definitionTextArea;

    @FXML
    private AnchorPane editFlashcardAnchorPane;

    @FXML
    private Text reviewDateLabel;

    @FXML
    private DatePicker reviewDatePicker;

    @FXML
    private Button saveChangesButton;

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
    void onSaveChangesClick(ActionEvent event) throws IOException {
        flashcard.setTerm(termTextField.getText());
        flashcard.setDefinition(definitionTextArea.getText());
        flashcard.setType(typeMenuButton.getText());
        String date = reviewDatePicker.getValue().format(formatter);
        flashcard.setReviewDate(date);

        CSVLoader.updateFlashcard(oldFlashcard, flashcard);

        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void returnToBrowserScene(ActionEvent event) throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;

        oldFlashcard.setTerm(flashcard.getTerm());
        oldFlashcard.setDefinition(flashcard.getDefinition());
        oldFlashcard.setType(flashcard.getType());
        oldFlashcard.setReviewDate(flashcard.getReviewDate());

        LocalDate date = LocalDate.parse(flashcard.getReviewDate(), formatter);

        termTextField.setText(flashcard.getTerm());
        definitionTextArea.setText(flashcard.getDefinition());
        typeMenuButton.setText(flashcard.getType());
        reviewDatePicker.setValue(date);
    }
}
