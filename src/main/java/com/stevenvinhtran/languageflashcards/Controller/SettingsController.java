package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import com.stevenvinhtran.languageflashcards.Model.SpacedRepetition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingsController {

    @FXML
    private Button backButton;
    @FXML
    private Label grammarLabel;
    @FXML
    private Spinner<Integer> grammarSpinner;
    @FXML
    private Label learningStepsLabel;
    @FXML
    private TextField learningStepsTextField;
    @FXML
    private Label relearningStepsLabel;
    @FXML
    private TextField relearningStepsTextField;
    @FXML
    private Button saveChangesButton;
    @FXML
    private AnchorPane settingsAnchorPane;
    @FXML
    private Label settingsLabel;
    @FXML
    private Label vocabularyLabel;
    @FXML
    private Spinner<Integer> vocabularySpinner;

    @FXML
    void onSaveChangesClick(ActionEvent event) throws IOException {
        new SpacedRepetition().updateSettings(vocabularySpinner.getValue(), grammarSpinner.getValue(), learningStepsTextField.getText(), relearningStepsTextField.getText());
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void returnToBrowserScene(ActionEvent event) throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    public void initialize() {
        SpacedRepetition spacedRepetition = new SpacedRepetition();

        vocabularySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, spacedRepetition.getNewVocabCardsPerDay()));
        grammarSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, spacedRepetition.getNewGrammarCardsPerDay()));

        vocabularySpinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{0,4}")) {
                return change;
            }
            return null;
        }));

        vocabularySpinner.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                vocabularySpinner.increment(0);
            }
        });

        grammarSpinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{0,4}")) {
                return change;
            }
            return null;
        }));

        grammarSpinner.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                grammarSpinner.increment(0);
            }
        });

        vocabularySpinner.setAccessibleText(String.valueOf(spacedRepetition.getNewVocabCardsPerDay()));
        grammarSpinner.setAccessibleText(String.valueOf(spacedRepetition.getNewGrammarCardsPerDay()));

        String learningSteps = "";
        for(int i = 0; i < spacedRepetition.getLearningSteps().length - 1; i++) {
            learningSteps += String.valueOf(spacedRepetition.getLearningSteps()[i]) + ",";
        }
        learningSteps += String.valueOf(spacedRepetition.getLearningSteps()[spacedRepetition.getLearningSteps().length - 1]);
        learningStepsTextField.setText(learningSteps);

        String relearningSteps = "";
        for(int i = 0; i < spacedRepetition.getRelearningSteps().length - 1; i++) {
            relearningSteps += String.valueOf(spacedRepetition.getLearningSteps()[i]) + ",";
        }
        relearningSteps += String.valueOf(spacedRepetition.getRelearningSteps()[spacedRepetition.getRelearningSteps().length - 1]);
        relearningStepsTextField.setText(relearningSteps);
    }
}
