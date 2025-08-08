package com.stevenvinhtran.languageflashcards.Model;

import java.time.LocalDateTime;

public class DummyFlashcard {
    private int index;
    private int repetitions;
    private boolean isNewCard;
    private boolean isRelearning;
    private LocalDateTime reviewDate;

    public DummyFlashcard(int index, int repetitions, boolean isNewCard, boolean isRelearning, LocalDateTime reviewDate) {
        this.index = index;
        this.repetitions = repetitions;
        this.isNewCard = isNewCard;
        this.isRelearning = isRelearning;
        this.reviewDate = reviewDate;
    }

    // Getters
    public int getIndex() { return index; }
    public int getRepetitions() { return repetitions; }
    public boolean getIsNewCard() { return isNewCard;}
    public boolean getIsRelearning() { return isRelearning; }
    public LocalDateTime getReviewDate() { return reviewDate; }

    // Setters
    public void setIndex(int index) { this.index = index; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setIsNewCard(boolean isNewCard) { this.isNewCard = isNewCard;}
    public void setIsRelearning(boolean isRelearning) { this.isRelearning = isRelearning; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
}
