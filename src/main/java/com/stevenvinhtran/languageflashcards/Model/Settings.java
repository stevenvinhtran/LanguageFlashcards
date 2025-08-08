package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public static int getNewVocabCardsPerDay() { return newVocabCardsPerDay; }
    public static int getNewGrammarCardsPerDay() { return newGrammarCardsPerDay; }
    public static int[] getLearningSteps() { return learningSteps; }
    public static int[] getRelearningSteps() { return relearningSteps; }

    // Setters
    public void setNewVocabCardsPerDay(int newVocabCardsPerDay) { Settings.newVocabCardsPerDay = newVocabCardsPerDay; }
    public void setNewGrammarCardsPerDay(int newGrammarCardsPerDay) { Settings.newGrammarCardsPerDay = newGrammarCardsPerDay; }
    public void setLearningSteps(int[] learningSteps) { Settings.learningSteps = learningSteps; }
    public void setRelearningSteps(int[] relearningSteps) { Settings.relearningSteps = relearningSteps; }

    public static void scheduleCards(List<Flashcard> flashcards) {
        LocalDateTime now = LocalDateTime.now();
        int newVocabDailyCount = getNumOfNewCardsCompletedToday("Vocabulary");
        int newGrammarDailyCount = getNumOfNewCardsCompletedToday("Grammar");
        // Handle new cards

        // New vocab
        List<Flashcard> newVocabCards = flashcards.stream()
                .filter(f -> f.getType().equals("Vocabulary") && f.getIsNewCard())
                .sorted(Comparator.comparing(Flashcard::getDateAdded))
                .collect(Collectors.toList());

        // New grammar
        List<Flashcard> newGrammarCards = flashcards.stream()
                .filter(f -> f.getType().equals("Grammar") && f.getIsNewCard())
                .sorted(Comparator.comparing(Flashcard::getDateAdded))
                .collect(Collectors.toList());

        int vocabDays = 0;
        boolean vocablimitAccountedFor = false;
        if (newVocabDailyCount >= newVocabCardsPerDay) { vocabDays++; }
        for (int i = 0; i < newVocabCards.size(); i++) {
            if (vocabDays > 0) { vocablimitAccountedFor = true; }

            if (!vocablimitAccountedFor) {
                if (newGrammarCardsPerDay - newVocabDailyCount == 0 || i % (newVocabCardsPerDay - newVocabDailyCount) == 0 && i != 0) {
                    vocabDays++;
                }
            } else {
                if (i % newVocabCardsPerDay == 0 && i != 0) {
                    vocabDays++;
                }
            }

            Flashcard card = newVocabCards.get(i);
            card.setReviewDate(now.plusDays(vocabDays).withHour(0).withMinute(0).withSecond(0));
        }

        int grammarDays = 0;
        boolean grammarlimitAccountedFor = false;
        if (newGrammarDailyCount >= newGrammarCardsPerDay) { grammarDays++; }
        for (int i = 0; i < newGrammarCards.size(); i++) {
            if (grammarDays > 0) { grammarlimitAccountedFor = true; }

            if (!grammarlimitAccountedFor) {
                if (newGrammarCardsPerDay - newGrammarDailyCount == 0 || i % (newGrammarCardsPerDay - newGrammarDailyCount) == 0 && i != 0) {
                    grammarDays++;
                }
            } else {
                if (i % newGrammarCardsPerDay == 0 && i != 0) {
                    grammarDays++;
                }
            }
            Flashcard card = newGrammarCards.get(i);
            card.setReviewDate(now.plusDays(grammarDays).withHour(0).withMinute(0).withSecond(0));
        }

        // Handle due cards
        List<Flashcard> dueNonNewCards = flashcards.stream()
                .filter(f -> !f.getIsNewCard() && f.getReviewDate().isBefore(now))
                .collect(Collectors.toList());

        for (Flashcard card : dueNonNewCards) {
            card.setReviewDate(now.withHour(0).withMinute(0).withSecond(0));
        }

        // Update CSV
        List<Flashcard> updatedCards = new ArrayList<>();
        updatedCards.addAll(newVocabCards);
        updatedCards.addAll(newGrammarCards);
        updatedCards.addAll(dueNonNewCards);

        CSVProcessor.updateDeck(flashcards, updatedCards);

        // Build Decks
        ArrayList<Flashcard> postUpdateCards = CSVProcessor.loadFlashcards();
        DeckHandler.buildVocabDeck(postUpdateCards);
        DeckHandler.buildGrammarDeck(postUpdateCards);
    }


    public static void updateSettings(int newVocabCardsPerDay, int newGrammarCardsPerDay,
                                      String learningStepsStr, String relearningStepsStr) {
        Settings.newVocabCardsPerDay = newVocabCardsPerDay;
        Settings.newGrammarCardsPerDay = newGrammarCardsPerDay;

        learningSteps = parseSteps(learningStepsStr, learningSteps, "Learning Steps");
        relearningSteps = parseSteps(relearningStepsStr, relearningSteps, "Relearning Steps");

        CSVProcessor.saveSettings(newVocabCardsPerDay, newGrammarCardsPerDay, learningSteps, relearningSteps);
        scheduleCards(CSVProcessor.loadFlashcards());
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

    private static int getNumOfNewCardsCompletedToday(String cardType) {
        int[] counts = CSVProcessor.getTodaysNewCardCounts();

        if (cardType.equals("Vocabulary")) {
            return counts[0];
        } else if (cardType.equals("Grammar")) {
            return counts[1];
        }
        return 0;
    }
}