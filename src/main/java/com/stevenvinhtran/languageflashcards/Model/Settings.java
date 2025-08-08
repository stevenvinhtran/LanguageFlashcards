package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {
    // Defaults
    private static int newVocabCardsPerDay = 20;
    private static int newGrammarCardsPerDay = 10;
    private static int[] learningSteps = {30, 1440, 4320}; // 30m, 1d, 3d
    private static int[] relearningSteps = {30, 1440}; // 30m, 1d

    // Getters
    public int getNewVocabCardsPerDay() {
        return newVocabCardsPerDay;
    }

    public int getNewGrammarCardsPerDay() {
        return newGrammarCardsPerDay;
    }

    public int[] getLearningSteps() {
        return learningSteps;
    }

    public int[] getRelearningSteps() {
        return relearningSteps;
    }

    // Setters
    public void setNewVocabCardsPerDay(int newVocabCardsPerDay) {
        Settings.newVocabCardsPerDay = newVocabCardsPerDay;
    }

    public void setNewGrammarCardsPerDay(int newGrammarCardsPerDay) {
        Settings.newGrammarCardsPerDay = newGrammarCardsPerDay;
    }

    public void setLearningSteps(int[] learningSteps) {
        Settings.learningSteps = learningSteps;
    }

    public void setRelearningSteps(int[] relearningSteps) {
        Settings.relearningSteps = relearningSteps;
    }

    public static void scheduleNewCards(List<Flashcard> flashcards) {
        LocalDateTime now = LocalDateTime.now();

        // Filter and sort new vocabulary cards by dateAdded (oldest first)
        List<Flashcard> newVocabCards = flashcards.stream()
                .filter(f -> f.getType().equals("Vocabulary") && f.getIsNewCard())
                .sorted(Comparator.comparing(Flashcard::getDateAdded))
                .collect(Collectors.toList());

        // Filter and sort new grammar cards by dateAdded (oldest first)
        List<Flashcard> newGrammarCards = flashcards.stream()
                .filter(f -> f.getType().equals("Grammar") && f.getIsNewCard())
                .sorted(Comparator.comparing(Flashcard::getDateAdded))
                .collect(Collectors.toList());

        int vocabDays = 0;
        for (int i = 0; i < newVocabCards.size(); i++) {
            if (i % newVocabCardsPerDay == 0 && i != 0) {
                vocabDays++;
            }
            Flashcard card = newVocabCards.get(i);
            card.setReviewDate(now.plusDays(vocabDays).withHour(0).withMinute(0));
        }

        int grammarDays = 0;
        for (int i = 0; i < newGrammarCards.size(); i++) {
            if (i % newGrammarCardsPerDay == 0 && i != 0) {
                grammarDays++;
            }
            Flashcard card = newGrammarCards.get(i);
            card.setReviewDate(now.plusDays(grammarDays).withHour(0).withMinute(0));
        }

        // Pointer is okay because its more efficient
        List<Flashcard> newFlashcards = newVocabCards;
        newFlashcards.addAll(newGrammarCards);
        CSVProcessor.updateDeck(flashcards, newFlashcards);
    }

    public static void updateSettings(int newVocabCardsPerDay, int newGrammarCardsPerDay,
                                      String learningStepsStr, String relearningStepsStr) {
        Settings.newVocabCardsPerDay = newVocabCardsPerDay;
        Settings.newGrammarCardsPerDay = newGrammarCardsPerDay;

        learningSteps = parseSteps(learningStepsStr, learningSteps, "Learning Steps");
        relearningSteps = parseSteps(relearningStepsStr, relearningSteps, "Relearning Steps");

        CSVProcessor.saveSettings(newVocabCardsPerDay, newGrammarCardsPerDay, learningSteps, relearningSteps);
        scheduleNewCards(CSVProcessor.loadFlashcards());
    }

    private static int[] parseSteps(String stepsStr, int[] currentSteps, String identifier) {
        try {
            String[] parts = stepsStr.split(",");
            int[] steps = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                steps[i] = Integer.parseInt(parts[i].trim());
            }
            return steps;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Learning Steps or Relearning Steps could not be parsed. Defaults will be applied.", "Error", JOptionPane.ERROR_MESSAGE);
            int[] steps;
            if (identifier.equals("Learning Steps")) {
                steps = new int[]{30, 1440, 4320};
            } else {
                steps = new int[]{30, 1440};
            }
            return steps;
        }
    }
}