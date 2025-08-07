package com.stevenvinhtran.languageflashcards.Model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;

public class Flashcard {
    private final SimpleStringProperty term;
    private final SimpleStringProperty definition;
    private final SimpleStringProperty type;
    private final SimpleObjectProperty<LocalDateTime> reviewDate;
    private final SimpleObjectProperty<LocalDateTime> dateAdded;
    private final SimpleObjectProperty<Integer> repetitions;
    private final SimpleObjectProperty<Double> easeFactor;
    private final SimpleObjectProperty<Integer> interval;

    public Flashcard(String term, String definition, String type,
                     LocalDateTime reviewDate, LocalDateTime dateAdded,
                     int repetitions, double easeFactor, int interval) {
        this.term = new SimpleStringProperty(term);
        this.definition = new SimpleStringProperty(definition);
        this.type = new SimpleStringProperty(type);
        this.reviewDate = new SimpleObjectProperty<>(reviewDate);
        this.dateAdded = new SimpleObjectProperty<>(dateAdded);
        this.repetitions = new SimpleObjectProperty<>(repetitions);
        this.easeFactor = new SimpleObjectProperty<>(easeFactor);
        this.interval = new SimpleObjectProperty<>(interval);
    }

    // Getters
    public String getTerm() { return term.get(); }
    public String getDefinition() { return definition.get(); }
    public String getType() { return type.get(); }
    public LocalDateTime getReviewDate() { return reviewDate.get(); }
    public LocalDateTime getDateAdded() { return dateAdded.get(); }
    public int getRepetitions() { return repetitions.get(); }
    public double getEaseFactor() { return easeFactor.get(); }
    public int getInterval() { return interval.get(); }

    // Setters
    public void setTerm(String term) { this.term.set(term); }
    public void setDefinition(String definition) { this.definition.set(definition); }
    public void setType(String type) { this.type.set(type); }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate.set(reviewDate); }
    public void setDateAdded(LocalDateTime dateAdded) { this.dateAdded.set(dateAdded); }
    public void setRepetitions(int repetitions) { this.repetitions.set(repetitions); }
    public void setEaseFactor(double easeFactor) { this.easeFactor.set(easeFactor); }
    public void setInterval(int interval) { this.interval.set(interval); }

    // Property getters
    public SimpleStringProperty termProperty() { return term; }
    public SimpleStringProperty definitionProperty() { return definition; }
    public SimpleStringProperty typeProperty() { return type; }
    public SimpleObjectProperty<LocalDateTime> reviewDateProperty() { return reviewDate; }
    public SimpleObjectProperty<LocalDateTime> dateAddedProperty() { return dateAdded; }
    public SimpleObjectProperty<Integer> repetitionsProperty() { return repetitions; }
    public SimpleObjectProperty<Double> easeFactorProperty() { return easeFactor; }
    public SimpleObjectProperty<Integer> intervalProperty() { return interval; }
}