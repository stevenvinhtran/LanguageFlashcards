package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class VocabularyController {
    ArrayList<Flashcard> flashcards = CSVProcessor.loadFlashcards();
    ArrayList<DummyFlashcard> dummyFlashcards = DeckHandler.vocabDummyDeck;
    private int newNum = 0;
    private int learningNum = 0;
    private int reviewingNum = 0;

    @FXML private Button easyButton;
    @FXML private Text easyDate;
    @FXML private Button failButton;
    @FXML private Text failDate;
    @FXML private Button passButton;
    @FXML private Text passDate;
    @FXML private Button revealButton;
    @FXML private Text termText;
    @FXML private Text definitionText;

    @FXML private Text newText;
    @FXML private Text learningText;
    @FXML private Text reviewingText;


    @FXML
    void onEasyClick() {
        switchVisible();
        if (dummyFlashcards.get(0).getIsNewCard()) {
            CSVProcessor.incrementDailyNewCardCount("Vocabulary");
        }
        SpacedRepetition.easyFlashcard(flashcards, dummyFlashcards);
        checkIfCompleted();
        refreshCounters();
    }

    @FXML
    void onFailClick() {
        switchVisible();
        if (dummyFlashcards.get(0).getIsNewCard()) {
            CSVProcessor.incrementDailyNewCardCount("Vocabulary");
        }
        SpacedRepetition.failFlashcard(flashcards, dummyFlashcards);
        checkIfCompleted();
        refreshCounters();
    }

    @FXML
    void onPassClick() {
        switchVisible();
        if (dummyFlashcards.get(0).getIsNewCard()) {
            CSVProcessor.incrementDailyNewCardCount("Vocabulary");
        }
        SpacedRepetition.passFlashcard(flashcards, dummyFlashcards);
        checkIfCompleted();
        refreshCounters();
    }

    @FXML
    void onRevealClick() {
        switchVisible();
    }

    @FXML
    void returnToHomeScene() throws IOException {
        new SceneSwitcher("home-view.fxml", "Home");
    }

    private void switchVisible() {
        revealButton.setVisible(!revealButton.isVisible());
        definitionText.setVisible(!definitionText.isVisible());
        failDate.setVisible(!failDate.isVisible());
        failButton.setVisible(!failButton.isVisible());
        passDate.setVisible(!passDate.isVisible());
        passButton.setVisible(!passButton.isVisible());
        easyDate.setVisible(!easyDate.isVisible());
        easyButton.setVisible(!easyButton.isVisible());
    }

    private void refreshCounters() {
        newNum = 0;
        learningNum = 0;
        reviewingNum = 0;

        for (DummyFlashcard dummyFlashcard : dummyFlashcards) {
            if (dummyFlashcard.getIsNewCard()) {
                newNum++;
            } else if (dummyFlashcard.getIsRelearning() || dummyFlashcard.getRepetitions() <= Settings.getLearningSteps().length) {
                learningNum++;
            } else {
                reviewingNum++;
            }
        }
        newText.setText("New : " + newNum);
        reviewingText.setText("Reviewing : " + reviewingNum);
        learningText.setText("Learning : " + learningNum);
    }

    private void setCardText() {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);

        termText.setText(flashcards.get(dummyFlashcard.getIndex()).getTerm());
        definitionText.setText(flashcards.get(dummyFlashcard.getIndex()).getDefinition());
        easyDate.setText(SpacedRepetition.predictEasyInterval(flashcards.get(dummyFlashcard.getIndex())));
        passDate.setText(SpacedRepetition.predictPassInterval(flashcards.get(dummyFlashcard.getIndex())));
        failDate.setText(SpacedRepetition.predictFailInterval(flashcards.get(dummyFlashcard.getIndex())));

        newText.setUnderline(false);
        learningText.setUnderline(false);
        reviewingText.setUnderline(false);
        if (dummyFlashcard.getIsNewCard()) {
            newText.setUnderline(true);
        } else if (dummyFlashcard.getIsRelearning() || dummyFlashcard.getRepetitions() <= Settings.getLearningSteps().length) {
            learningText.setUnderline(true);
        } else {
            reviewingText.setUnderline(true);
        }
    }

    private void checkIfCompleted() {
        if (!dummyFlashcards.isEmpty()) {
            setCardText();
        } else {
            termText.setText("Completed Vocabulary Deck!");

            revealButton.setVisible(false);
            definitionText.setVisible(false);
            failDate.setVisible(false);
            failButton.setVisible(false);
            passDate.setVisible(false);
            passButton.setVisible(false);
            easyDate.setVisible(false);
            easyButton.setVisible(false);
        }
    }

    public void initialize() {
        refreshCounters();
        checkIfCompleted();
    }
}
