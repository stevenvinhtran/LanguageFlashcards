package com.stevenvinhtran.languageflashcards.Model;

import java.time.LocalDateTime;

public class SpacedRepetition {
    Settings settings = new Settings();
    int[] learningSteps = settings.getLearningSteps();
    int[] relearningSteps = settings.getRelearningSteps();

    public void easyFlashcard(Flashcard flashcard) {
        int repetitions = flashcard.getRepetitions();
        repetitions++;
        double easeFactor = flashcard.getEaseFactor() + (0.1 * repetitions);
        if (easeFactor > 3.0) {
            easeFactor = 3.0;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(repetitions);

        if (flashcard.getIsNewCard()) {
            learningPass(flashcard);
            flashcard.setIsNewCard(false);
        } else if (!flashcard.getIsRelearning() && repetitions <= learningSteps.length) {
            learningPass(flashcard);
        } else if (flashcard.getIsRelearning() && repetitions <= relearningSteps.length) {
            relearningPass(flashcard);
        } else {
            flashcard.setInterval((int)Math.round(flashcard.getInterval() * flashcard.getEaseFactor()));
            flashcard.setReviewDate(LocalDateTime.now().plusDays(flashcard.getInterval()));
        }
    }

    public void passFlashcard(Flashcard flashcard) {
        int repetitions = flashcard.getRepetitions();
        repetitions++;
        double easeFactor = flashcard.getEaseFactor() + (0.1 * repetitions);
        if (easeFactor > 2.5) {
            easeFactor = 2.5;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(repetitions);

        if (flashcard.getIsNewCard()) {
            learningPass(flashcard);
            flashcard.setIsNewCard(false);
        } else if (!flashcard.getIsRelearning() && repetitions <= learningSteps.length) {
            learningPass(flashcard);
        } else if (flashcard.getIsRelearning() && repetitions <= relearningSteps.length) {
            relearningPass(flashcard);
        } else {
            flashcard.setInterval((int)Math.round(flashcard.getInterval() * flashcard.getEaseFactor()));
            flashcard.setReviewDate(LocalDateTime.now().plusDays(flashcard.getInterval()));
        }
    }

    public void failFlashcard(Flashcard flashcard) {
        double easeFactor = flashcard.getEaseFactor() - 0.35;
        if (easeFactor < 1.3) {
            easeFactor = 1.3;
        }
        flashcard.setEaseFactor(easeFactor);
        flashcard.setRepetitions(0);
        flashcard.setInterval(0);
        flashcard.setReviewDate(LocalDateTime.now().plusMinutes(10));
        flashcard.setIsRelearning(true);
    }

    public void learningPass(Flashcard flashcard) {
        for (int i = 0; i < learningSteps.length; i++) {
            if (i == flashcard.getRepetitions() - 1) {
                // If the step is greater or equal to a day
                if (learningSteps[i] >= 1440) {
                    flashcard.setInterval(learningSteps[i]/1440);
                    flashcard.setReviewDate(LocalDateTime.now().plusDays(flashcard.getInterval()));
                } else {
                    flashcard.setInterval(0);
                    flashcard.setReviewDate(LocalDateTime.now().plusMinutes(learningSteps[i]));
                }
            }
        }
    }

    public void relearningPass(Flashcard flashcard) {
        for (int i = 0; i < relearningSteps.length; i++) {
            if (i == flashcard.getRepetitions() - 1) {
                // If the step is greater or equal to a day
                if (relearningSteps[i] >= 1440) {
                    flashcard.setInterval(relearningSteps[i]/1440);
                    flashcard.setReviewDate(LocalDateTime.now().plusDays(flashcard.getInterval()));
                } else {
                    flashcard.setInterval(0);
                    flashcard.setReviewDate(LocalDateTime.now().plusMinutes(relearningSteps[i]));
                }

                if (i == relearningSteps.length - 1) {
                    flashcard.setIsRelearning(false);
                }
            }
        }
    }
}
