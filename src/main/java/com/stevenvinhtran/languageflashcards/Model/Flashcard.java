package com.stevenvinhtran.languageflashcards.Model;

import javafx.beans.property.SimpleStringProperty;

public class Flashcard {
    private final SimpleStringProperty term;
    private final SimpleStringProperty definition;
    private final SimpleStringProperty type;
    private final SimpleStringProperty reviewDate;

    public Flashcard(String term, String definition, String type, String reviewDate) {
        this.term = new SimpleStringProperty(term);
        this.definition = new SimpleStringProperty(definition);
        this.type = new SimpleStringProperty(type);
        this.reviewDate = new SimpleStringProperty(reviewDate);
    }

    public String getTerm() { return term.get(); }
    public String getDefinition() { return definition.get(); }
    public String getType() { return type.get(); }
    public String getReviewDate() { return reviewDate.get(); }

    public void setTerm(String term) { this.term.set(term); }
    public void setDefinition(String definition) { this.definition.set(definition); }
    public void setType(String type) { this.type.set(type); }
    public void setReviewDate(String reviewDate) { this.reviewDate.set(reviewDate); }

    public SimpleStringProperty termProperty() { return term; }
    public SimpleStringProperty definitionProperty() { return definition; }
    public SimpleStringProperty typeProperty() { return type; }
    public SimpleStringProperty reviewDateProperty() { return reviewDate; }
}
