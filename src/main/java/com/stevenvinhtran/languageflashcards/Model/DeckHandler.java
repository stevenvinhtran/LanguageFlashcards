package com.stevenvinhtran.languageflashcards.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeckHandler {
    public static ArrayList<DummyFlashcard> vocabDummyDeck = new ArrayList<DummyFlashcard>();
    public static ArrayList<DummyFlashcard> grammarDummyDeck = new ArrayList<DummyFlashcard>();

    public static void buildVocabDeck(List<Flashcard> flashcards) {
        ArrayList<DummyFlashcard> dummyDeck = new ArrayList<DummyFlashcard>();

        for (int i = 0; i < flashcards.size(); i++) {
            Flashcard flashcard = flashcards.get(i);
            LocalDate reviewDay = flashcard.getReviewDate().toLocalDate();

            if (reviewDay.equals(LocalDate.now()) && flashcard.getType().equals("Vocabulary")) {
                DummyFlashcard dummyFlashcard = new DummyFlashcard(
                        i,
                        flashcard.getRepetitions(),
                        flashcard.getIsNewCard(),
                        flashcard.getIsRelearning(),
                        flashcard.getReviewDate()
                );
                dummyDeck.add(dummyFlashcard);
            }
        }

        Collections.shuffle(dummyDeck);
        sortDummyDeck(dummyDeck);
        vocabDummyDeck = dummyDeck;
    }

    public static void buildGrammarDeck(List<Flashcard> flashcards) {
        ArrayList<DummyFlashcard> dummyDeck = new ArrayList<DummyFlashcard>();

        for (int i = 0; i < flashcards.size(); i++) {
            Flashcard flashcard = flashcards.get(i);
            LocalDate reviewDay = flashcard.getReviewDate().toLocalDate();

            if (reviewDay.equals(LocalDate.now()) && flashcard.getType().equals("Grammar")) {
                DummyFlashcard dummyFlashcard = new DummyFlashcard(
                        i,
                        flashcard.getRepetitions(),
                        flashcard.getIsNewCard(),
                        flashcard.getIsRelearning(),
                        flashcard.getReviewDate()
                );
                dummyDeck.add(dummyFlashcard);
            }
        }

        Collections.shuffle(dummyDeck);
        sortDummyDeck(dummyDeck);
        grammarDummyDeck = dummyDeck;
    }

    public static void sortDummyDeck(ArrayList<DummyFlashcard> dummyDeck) {
        LocalDateTime now = LocalDateTime.now();

        List<DummyFlashcard> earlierOrNow = new ArrayList<>();
        List<DummyFlashcard> midnightToday = new ArrayList<>();
        List<DummyFlashcard> later = new ArrayList<>();

        for (DummyFlashcard card : dummyDeck) {
            LocalDateTime review = card.getReviewDate();

            boolean isMidnightToday = review.toLocalDate().equals(LocalDate.now()) && review.getHour() == 0 && review.getMinute() == 0;

            if (isMidnightToday) {
                midnightToday.add(card);
            } else if (!review.isAfter(now)) {
                earlierOrNow.add(card);
            } else {
                later.add(card);
            }
        }

        Comparator<DummyFlashcard> byDate = Comparator.comparing(DummyFlashcard::getReviewDate);
        earlierOrNow.sort(byDate);
        midnightToday.sort(byDate);
        later.sort(byDate);

        dummyDeck.clear();
        dummyDeck.addAll(earlierOrNow);
        dummyDeck.addAll(midnightToday);
        dummyDeck.addAll(later);
    }
}
