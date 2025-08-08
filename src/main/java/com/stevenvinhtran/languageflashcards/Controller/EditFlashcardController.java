package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;

public class EditFlashcardController {
    private Flashcard flashcard;
    private Flashcard oldFlashcard;

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
    @FXML
    private Label sm2SettingsLabel;
    @FXML
    private Label sm2WarningLabel;
    @FXML
    private Text repetitionsText;
    @FXML
    private Text easeFactorText;
    @FXML
    private Text intervalText;
    @FXML
    private Text defaultsText;
    @FXML private MenuButton typeMenuButton;
    @FXML private MenuItem vocabularyMenuItem;
    @FXML private MenuItem grammarMenuItem;
    @FXML private TextField repetitionsField;
    @FXML private TextField easeFactorField;
    @FXML private TextField intervalField;

    @FXML void onVocabularyItemSelect() { typeMenuButton.setText(vocabularyMenuItem.getText()); }
    @FXML void onGrammarItemSelect() { typeMenuButton.setText(grammarMenuItem.getText()); }

    @FXML
    void onSaveChangesClick() throws IOException {
        flashcard.setTerm(termTextField.getText());
        flashcard.setDefinition(definitionTextArea.getText());
        flashcard.setType(typeMenuButton.getText());
        flashcard.setReviewDate(reviewDatePicker.getValue().atStartOfDay());
        flashcard.setRepetitions(Integer.parseInt(repetitionsField.getText()));
        flashcard.setEaseFactor(Double.parseDouble(easeFactorField.getText()));
        flashcard.setInterval(Integer.parseInt(intervalField.getText()));
        // isNewCard will not be changed
        // isRelearning will not be changed

        CSVProcessor.updateFlashcard(oldFlashcard, flashcard);
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void returnToBrowserScene() throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        this.oldFlashcard = new Flashcard(
                flashcard.getTerm(),
                flashcard.getDefinition(),
                flashcard.getType(),
                flashcard.getReviewDate(),
                flashcard.getDateAdded(),
                flashcard.getRepetitions(),
                flashcard.getEaseFactor(),
                flashcard.getInterval(),
                flashcard.getIsNewCard(),
                flashcard.getIsRelearning()
        );

        termTextField.setText(flashcard.getTerm());
        definitionTextArea.setText(flashcard.getDefinition());
        typeMenuButton.setText(flashcard.getType());
        reviewDatePicker.setValue(flashcard.getReviewDate().toLocalDate());
        repetitionsField.setText(String.valueOf(flashcard.getRepetitions()));
        easeFactorField.setText(String.valueOf(flashcard.getEaseFactor()));
        intervalField.setText(String.valueOf(flashcard.getInterval()));
        // isNewCard will not be changed, so there is no need to have it here
        // isRelearning will not be changed, so there is no need to have it here
    }
}