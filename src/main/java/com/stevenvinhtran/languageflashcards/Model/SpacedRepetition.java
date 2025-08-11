package com.stevenvinhtran.languageflashcards.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SpacedRepetition {
    private static Settings settings = new Settings();
    private static int[] learningSteps = settings.getLearningSteps();
    private static int[] relearningSteps = settings.getRelearningSteps();

    public static void easyFlashcard(ArrayList<Flashcard> flashcards, ArrayList<DummyFlashcard> dummyFlashcards) {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);
        Flashcard flashcard = flashcards.get(dummyFlashcard.getIndex());
        Flashcard oldFlashcard = new Flashcard(
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

        int repetitions = flashcard.getRepetitions();
        repetitions++;
        double easeFactor = flashcard.getEaseFactor() + (0.1 * repetitions);
        if (easeFactor > 3.0) {
            easeFactor = 3.0;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(repetitions);
        dummyFlashcard.setRepetitions(repetitions);

        if (flashcard.getIsNewCard()) {
            learningPass(flashcard, dummyFlashcards);
        } else if (!flashcard.getIsRelearning() && repetitions <= learningSteps.length) {
            learningPass(flashcard, dummyFlashcards);
        } else if (flashcard.getIsRelearning() && repetitions <= relearningSteps.length) {
            relearningPass(flashcard, dummyFlashcards);
        } else {
            flashcard.setInterval((int)Math.round(flashcard.getInterval() * flashcard.getEaseFactor()));
            flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).plusDays(flashcard.getInterval()));
            flashcard.setIsRelearning(false);
            dummyFlashcards.remove(0);
        }

        DeckHandler.sortDummyDeck(dummyFlashcards);

        CSVProcessor.updateFlashcard(oldFlashcard, flashcard);
    }

    public static void passFlashcard(ArrayList<Flashcard> flashcards, ArrayList<DummyFlashcard> dummyFlashcards) {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);
        Flashcard flashcard = flashcards.get(dummyFlashcard.getIndex());
        Flashcard oldFlashcard = new Flashcard(
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

        int repetitions = flashcard.getRepetitions();
        repetitions++;
        double easeFactor = flashcard.getEaseFactor() + (0.1 * repetitions);
        if (easeFactor > 2.5) {
            easeFactor = 2.5;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(repetitions);
        dummyFlashcard.setRepetitions(repetitions);

        if (flashcard.getIsNewCard()) {
            learningPass(flashcard, dummyFlashcards);
        } else if (!flashcard.getIsRelearning() && repetitions <= learningSteps.length) {
            learningPass(flashcard, dummyFlashcards);
        } else if (flashcard.getIsRelearning() && repetitions <= relearningSteps.length) {
            relearningPass(flashcard, dummyFlashcards);
        } else {
            flashcard.setInterval((int)Math.round(flashcard.getInterval() * flashcard.getEaseFactor()));
            flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).plusDays(flashcard.getInterval()));
            flashcard.setIsRelearning(false);
            dummyFlashcards.remove(0);
        }

        DeckHandler.sortDummyDeck(dummyFlashcards);

        CSVProcessor.updateFlashcard(oldFlashcard, flashcard);
    }

    public static void failFlashcard(ArrayList<Flashcard> flashcards, ArrayList<DummyFlashcard> dummyFlashcards) {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);
        Flashcard flashcard = flashcards.get(dummyFlashcard.getIndex());
        Flashcard oldFlashcard = new Flashcard(
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

        double easeFactor = flashcard.getEaseFactor() - 0.35;
        if (easeFactor < 1.3) {
            easeFactor = 1.3;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(0);
        flashcard.setInterval(0);
        flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        flashcard.setIsNewCard(false);
        flashcard.setIsRelearning(true);

        dummyFlashcard.setRepetitions(0);
        dummyFlashcard.setReviewDate(LocalDateTime.now().plusMinutes(10)); // See the failed card 10 minutes later
        dummyFlashcard.setIsNewCard(false);
        dummyFlashcard.setIsRelearning(true);
        DeckHandler.sortDummyDeck(dummyFlashcards);

        CSVProcessor.updateFlashcard(oldFlashcard, flashcard);
    }

    private static void learningPass(Flashcard flashcard, ArrayList<DummyFlashcard> dummyFlashcards) {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);

        for (int i = 0; i < learningSteps.length; i++) {
            if (i == flashcard.getRepetitions() - 1) {
                // If the step is greater or equal to a day
                if (learningSteps[i] >= 1440) {
                    flashcard.setInterval(learningSteps[i]/1440);
                    flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).plusDays(flashcard.getInterval()));
                    dummyFlashcards.remove(0);
                } else {
                    flashcard.setInterval(0);
                    flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
                    dummyFlashcard.setReviewDate(LocalDateTime.now().plusMinutes(learningSteps[i]));
                }
            }

            if (i == 0) {
                flashcard.setIsNewCard(false);
                dummyFlashcard.setIsNewCard(false);
            }
        }
    }

    private static void relearningPass(Flashcard flashcard, ArrayList<DummyFlashcard> dummyFlashcards) {
        DummyFlashcard dummyFlashcard = dummyFlashcards.get(0);

        for (int i = 0; i < relearningSteps.length; i++) {
            if (i == flashcard.getRepetitions() - 1) {
                // If the step is greater or equal to a day
                if (relearningSteps[i] >= 1440) {
                    flashcard.setInterval(relearningSteps[i]/1440);
                    flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).plusDays(flashcard.getInterval()));
                    dummyFlashcards.remove(0);
                } else {
                    flashcard.setInterval(0);
                    flashcard.setReviewDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
                    dummyFlashcard.setReviewDate(LocalDateTime.now().plusMinutes(relearningSteps[i]));
                }

                if (i == relearningSteps.length - 1) {
                    flashcard.setIsRelearning(false);
                    dummyFlashcard.setIsRelearning(false);
                }
            }
        }
    }

    public static String predictEasyInterval(Flashcard flashcard) {
        double easeFactor = Math.min(flashcard.getEaseFactor() + (0.1 * (flashcard.getRepetitions() + 1)), 3.0);
        int newIntervalMinutes = calculateProjectedInterval(flashcard, easeFactor);
        return formatInterval(flashcard, newIntervalMinutes);
    }

    public static String predictPassInterval(Flashcard flashcard) {
        double easeFactor = Math.min(flashcard.getEaseFactor() + (0.1 * (flashcard.getRepetitions() + 1)), 2.5);
        int newIntervalMinutes = calculateProjectedInterval(flashcard, easeFactor);
        return formatInterval(flashcard, newIntervalMinutes);
    }

    public static String predictFailInterval(Flashcard flashcard) {
        return "10 minutes";
    }

    private static int calculateProjectedInterval(Flashcard flashcard, double easeFactor) {
        int repetitions = flashcard.getRepetitions() + 1;

        if (flashcard.getIsNewCard()) {
            return learningSteps.length > 0 ? learningSteps[0] : 30; // First learning step
        } else if (!flashcard.getIsRelearning() && repetitions <= learningSteps.length) {
            return learningSteps[repetitions - 1]; // Current learning step
        } else if (flashcard.getIsRelearning() && repetitions <= relearningSteps.length) {
            return relearningSteps[repetitions - 1]; // Current relearning step
        } else {
            return (int) Math.round((flashcard.getInterval() * 1440) * easeFactor); // Normal interval in minutes
        }
    }

    private static String formatInterval(Flashcard flashcard, int intervalMinutes) {
        // Convert minutes to days for easier formatting
        double days = intervalMinutes / (24.0 * 60.0);
        if (intervalMinutes < 1440) { // Less than 1 day
            return intervalMinutes + (intervalMinutes == 1 ? " minute" : " minutes");
        } else if (days < 30) {
            int wholeDays = (int) Math.round(days);
            return wholeDays + (wholeDays == 1 ? " day" : " days");
        } else if (days < 365) {
            double months = Math.round(days / 30.0 * 10) / 10.0;
            return months + (months == 1.0 ? " month" : " months");
        } else {
            double years = Math.round(days / 365.0 * 10) / 10.0;
            return years + (years == 1.0 ? " year" : " years");
        }
    }
}
