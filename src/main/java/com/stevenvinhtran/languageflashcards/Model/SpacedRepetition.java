package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SpacedRepetition {
    // Defaults
    private static int newVocabCardsPerDay = 20;
    private static int newGrammarCardsPerDay = 10;
    private static int[] learningSteps = {30, 1440, 4320}; // 30m, 1d, 3d
    private static int[] relearningSteps = {30, 1440}; // 30m, 1d


    // Getters
    public int getNewVocabCardsPerDay() { return newVocabCardsPerDay; }
    public int getNewGrammarCardsPerDay() { return newGrammarCardsPerDay; }
    public int[] getLearningSteps() { return learningSteps; }
    public int[] getRelearningSteps() { return relearningSteps; }

    // Setters
    public void setNewVocabCardsPerDay(int newVocabCardsPerDay) { SpacedRepetition.newVocabCardsPerDay = newVocabCardsPerDay; }
    public void setNewGrammarCardsPerDay(int newGrammarCardsPerDay) { SpacedRepetition.newGrammarCardsPerDay = newGrammarCardsPerDay; }
    public void setLearningSteps(int[] learningSteps) { SpacedRepetition.learningSteps = learningSteps; }
    public void setRelearningSteps(int[] relearningSteps) { SpacedRepetition.relearningSteps = relearningSteps; }

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
        SpacedRepetition.newVocabCardsPerDay = newVocabCardsPerDay;
        SpacedRepetition.newGrammarCardsPerDay = newGrammarCardsPerDay;

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
            JOptionPane.showMessageDialog(null, "Learning Steps or Relearning Steps could not be parsed. Please only use numbers and commas.", "Error", JOptionPane.ERROR_MESSAGE);
            int[] steps;
            if (identifier.equals("Learning Steps")) {
                steps = new int[]{30, 1440, 4320};
            } else {
                steps = new int[]{30, 1440};
            }
            return steps;
        }
    }


//    // Processes a review response and updates the card's SRS parameters
//    public void processReview(Flashcard card, ReviewResponse response) {
//        LocalDateTime now = LocalDateTime.now();
//
//        if (card.getRepetitions() == 0) {
//            // Card is in learning phase
//            processLearningPhase(card, response, now);
//        } else if (response == ReviewResponse.FAIL) {
//            // Card failed - move to relearning
//            processRelearningPhase(card, now);
//        } else {
//            // Normal review - apply SM-2 algorithm
//            processNormalReview(card, response, now);
//        }
//    }
//
//    private void processLearningPhase(Flashcard card, ReviewResponse response, LocalDateTime now) {
//        if (response == ReviewResponse.FAIL) {
//            // Failed learning step - reset to first step
//            card.setInterval(0);
//            card.setReviewDate(now.plusMinutes(learningSteps[0]));
//        } else {
//            // Passed learning step - move to next step or graduate
//            int currentStep = card.getInterval();
//            if (currentStep < learningSteps.length - 1) {
//                // Move to next learning step
//                card.setInterval(currentStep + 1);
//                card.setReviewDate(now.plusMinutes(learningSteps[currentStep + 1]));
//            } else {
//                // Graduate from learning phase
//                card.setRepetitions(1);
//                card.setInterval(1); // First interval after learning
//                card.setReviewDate(now.plusDays(card.getInterval()));
//            }
//        }
//    }
//
//    private void processRelearningPhase(Flashcard card, LocalDateTime now) {
//        // Reset to first relearning step
//        card.setRepetitions(0);
//        card.setInterval(0);
//        card.setEaseFactor(Math.max(1.3, card.getEaseFactor() - 0.2)); // Reduce ease factor
//        card.setReviewDate(now.plusMinutes(relearningSteps[0]));
//    }
//
//    private void processNormalReview(Flashcard card, ReviewResponse response, LocalDateTime now) {
//        // Update ease factor based on response
//        switch (response) {
//            case EASY:
//                card.setEaseFactor(card.getEaseFactor() + 0.15);
//                break;
//            case FAIL:
//                // Shouldn't happen here, handled in processRelearningPhase
//                break;
//            case PASS:
//                // No change to ease factor
//                break;
//        }
//
//        // Cap ease factor between 1.3 and 2.5
//        card.setEaseFactor(Math.max(1.3, Math.min(2.5, card.getEaseFactor())));
//
//        // Update repetitions and interval
//        card.setRepetitions(card.getRepetitions() + 1);
//
//        if (card.getRepetitions() == 1) {
//            card.setInterval(1);
//        } else if (card.getRepetitions() == 2) {
//            card.setInterval(3);
//        } else {
//            card.setInterval((int) Math.round(card.getInterval() * card.getEaseFactor()));
//        }
//
//        // Schedule next review
//        card.setReviewDate(now.plusDays(card.getInterval()));
//    }
//
//    public enum ReviewResponse {
//        PASS, FAIL, EASY
//    }
}