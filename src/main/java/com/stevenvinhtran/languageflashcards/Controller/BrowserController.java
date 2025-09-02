package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.io.IOException;

public class BrowserController {
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML private TextField searchField;
    @FXML private Text numberOfCardsText;

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

    @FXML void setTodayMenuItem() {sortByMenu.setText(todayMenuItem.getText()); loadFlashcardTable();};
    @FXML void setThreeDaysMenuItem() {sortByMenu.setText(threeDaysMenuItem.getText()); loadFlashcardTable();};
    @FXML void setSevenDaysMenuItem() {sortByMenu.setText(sevenDaysMenuItem.getText()); loadFlashcardTable();};
    @FXML void setThirtyDaysMenuItem() {sortByMenu.setText(thirtyDaysMenuItem.getText()); loadFlashcardTable();};
    @FXML void setThreeMonthsMenuItem() {sortByMenu.setText(threeMonthsMenuItem.getText()); loadFlashcardTable();};
    @FXML void setSixMonthsMenuItem() {sortByMenu.setText(sixMonthsMenuItem.getText()); loadFlashcardTable();};
    @FXML void setTwelveMonthsMenuItem() {sortByMenu.setText(twelveMonthsMenuItem.getText()); loadFlashcardTable();};
    @FXML void setAllReviewsMenuItem() {sortByMenu.setText(allReviewsMenuItem.getText()); loadFlashcardTable();};

    @FXML
    public void loadFlashcardTable() {
        ObservableList<Flashcard> sortedFlashcards = FXCollections.observableList(CSVProcessor.loadFlashcards());
        LocalDate now = LocalDate.now();
        String selection = sortByMenu.getText();
        int daysToSearch = 0;

        if (!selection.equals("All Reviews")) {
            switch (selection) {
                case "Today":
                    break;
                case "Next 3 days":
                    daysToSearch = 3;
                    break;
                case "Next 7 days":
                    daysToSearch = 7;
                    break;
                case "Next 30 days":
                    daysToSearch = 30;
                    break;
                case "Next 3 months":
                    daysToSearch = 90;
                    break;
                case "Next 6 months":
                    daysToSearch = 183;
                    break;
                case "Next 12 months":
                    daysToSearch = 365;
                    break;
            }

            for (int i = 0; i < sortedFlashcards.size(); i++) {
                LocalDate latestDay = sortedFlashcards.get(i).getReviewDate().toLocalDate();
                if (!latestDay.equals(now.plusDays(daysToSearch)) && !latestDay.isBefore(now.plusDays(daysToSearch))) {
                    sortedFlashcards.remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < sortedFlashcards.size(); i++) {
            String term = sortedFlashcards.get(i).getTerm().replaceAll("\\s+", "").toLowerCase();
            String definition = sortedFlashcards.get(i).getDefinition().replaceAll("\\s+", "").toLowerCase();
            String search = searchField.getText().replaceAll("\\s+", "").toLowerCase();

            if (!term.contains(search) && !definition.contains(search)) {
                sortedFlashcards.remove(i);
                i--;
            }
        }

        numberOfCardsText.setText(sortedFlashcards.size() + " Cards");
        flashcardsTable.setItems(sortedFlashcards);
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