package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import com.stevenvinhtran.languageflashcards.Model.Settings;
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
        new Settings().updateSettings(vocabularySpinner.getValue(), grammarSpinner.getValue(), learningStepsTextField.getText(), relearningStepsTextField.getText());
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    @FXML
    void returnToBrowserScene(ActionEvent event) throws IOException {
        new SceneSwitcher("browser-view.fxml", "Browser");
    }

    public void initialize() {
        Settings settings = new Settings();

        vocabularySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, settings.getNewVocabCardsPerDay()));
        grammarSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, settings.getNewGrammarCardsPerDay()));

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

        vocabularySpinner.setAccessibleText(String.valueOf(settings.getNewVocabCardsPerDay()));
        grammarSpinner.setAccessibleText(String.valueOf(settings.getNewGrammarCardsPerDay()));

        String learningSteps = "";
        for(int i = 0; i < settings.getLearningSteps().length - 1; i++) {
            learningSteps += String.valueOf(settings.getLearningSteps()[i]) + ",";
        }
        learningSteps += String.valueOf(settings.getLearningSteps()[settings.getLearningSteps().length - 1]);
        learningStepsTextField.setText(learningSteps);

        String relearningSteps = "";
        for(int i = 0; i < settings.getRelearningSteps().length - 1; i++) {
            relearningSteps += String.valueOf(settings.getLearningSteps()[i]) + ",";
        }
        relearningSteps += String.valueOf(settings.getRelearningSteps()[settings.getRelearningSteps().length - 1]);
        relearningStepsTextField.setText(relearningSteps);
    }
}
