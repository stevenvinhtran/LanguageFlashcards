package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.io.IOException;

public class BrowserController {
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private Button addButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane browserAnchorPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button editButton;
    @FXML
    private Label sortByLabel;
    @FXML private SplitMenuButton sortByMenu;
    @FXML private MenuItem todayMenuItem;
    @FXML private MenuItem threeDaysMenuItem;
    @FXML private MenuItem sevenDaysMenuItem;
    @FXML private MenuItem thirtyDaysMenuItem;
    @FXML private MenuItem threeMonthsMenuItem;
    @FXML private MenuItem sixMonthsMenuItem;
    @FXML private MenuItem twelveMonthsMenuItem;
    @FXML private MenuItem allReviewsMenuItem;

    @FXML private TableView<Flashcard> flashcardsTable;
    @FXML private TableColumn<Flashcard, String> termColumn;
    @FXML private TableColumn<Flashcard, String> definitionColumn;
    @FXML private TableColumn<Flashcard, String> typeColumn;
    @FXML private TableColumn<Flashcard, String> reviewDateColumn;
    @FXML private TableColumn<Flashcard, String> dateAddedColumn;

    @FXML
    void deleteTerm() {
        Flashcard selectedFlashcard = flashcardsTable.getSelectionModel().getSelectedItem();
        CSVProcessor.deleteFlashcard(selectedFlashcard);
        loadFlashcardTable();
    }

    @FXML
    void goToAddTermScene() throws IOException {
        new SceneSwitcher("add-flashcard-view.fxml", "Add Term");
    }

    @FXML
    void goToEditTermScene() throws IOException {
        Flashcard selectedFlashcard = flashcardsTable.getSelectionModel().getSelectedItem();
        if (selectedFlashcard != null) {
            new SceneSwitcher(selectedFlashcard);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a flashcard to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void goToSettingsScene() throws IOException {
        new SceneSwitcher("settings-view.fxml", "Settings");
    }

    @FXML
    void returnToHomeScene() throws IOException {
        new SceneSwitcher("home-view.fxml", "Home");
    }

    @FXML void setTodayMenuItem() {sortByMenu.setText(todayMenuItem.getText()); sortFlashcards();};
    @FXML void setThreeDaysMenuItem() {sortByMenu.setText(threeDaysMenuItem.getText()); sortFlashcards();};
    @FXML void setSevenDaysMenuItem() {sortByMenu.setText(sevenDaysMenuItem.getText()); sortFlashcards();};
    @FXML void setThirtyDaysMenuItem() {sortByMenu.setText(thirtyDaysMenuItem.getText()); sortFlashcards();};
    @FXML void setThreeMonthsMenuItem() {sortByMenu.setText(threeMonthsMenuItem.getText()); sortFlashcards();};
    @FXML void setSixMonthsMenuItem() {sortByMenu.setText(sixMonthsMenuItem.getText()); sortFlashcards();};
    @FXML void setTwelveMonthsMenuItem() {sortByMenu.setText(twelveMonthsMenuItem.getText()); sortFlashcards();};
    @FXML void setAllReviewsMenuItem() {sortByMenu.setText(allReviewsMenuItem.getText()); sortFlashcards();};

    @FXML
    void sortFlashcards() {
        ObservableList<Flashcard> sortedFlashcards = FXCollections.observableList(CSVProcessor.loadFlashcards());
        String selection = sortByMenu.getText();
        System.out.println(selection);
        // All Flashcards, Today, Next 7 Days, Next 30 days, Next 3 months, Next 6 months, Next 12 months
    }

    @FXML
    public void loadFlashcardTable() {
        sortByMenu.setText(allReviewsMenuItem.getText());
        ObservableList<Flashcard> flashcards = FXCollections.observableList(CSVProcessor.loadFlashcards());
        flashcardsTable.setItems(flashcards);
    }

    @FXML
    public void initialize() {
        termColumn.setCellValueFactory(cellData -> cellData.getValue().termProperty());
        definitionColumn.setCellValueFactory(cellData -> cellData.getValue().definitionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        // Format dates for display
        reviewDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReviewDate().format(displayFormatter)));
        dateAddedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateAdded().format(displayFormatter)));

        // loadFlashcardTable is called in SceneSwitcher
    }
}