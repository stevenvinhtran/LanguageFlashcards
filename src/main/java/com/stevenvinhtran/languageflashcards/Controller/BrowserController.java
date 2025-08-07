package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.CSVProcessor;
import com.stevenvinhtran.languageflashcards.Model.Flashcard;
import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.io.IOException;

public class BrowserController {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private AnchorPane browserAnchorPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label sortByLabel;

    @FXML
    private SplitMenuButton sortByMenu;

    @FXML private TableView<Flashcard> flashcardsTable;
    @FXML private TableColumn<Flashcard, String> termColumn;
    @FXML private TableColumn<Flashcard, String> definitionColumn;
    @FXML private TableColumn<Flashcard, String> typeColumn;
    @FXML private TableColumn<Flashcard, String> reviewDateColumn;

    @FXML
    void deleteTerm(ActionEvent event) {
        Flashcard selectedFlashcard = flashcardsTable.getSelectionModel().getSelectedItem();
        CSVProcessor.deleteFlashcard(selectedFlashcard);
        loadFlashcardTable();
    }

    @FXML
    void goToAddTermScene(ActionEvent event) throws IOException {
        new SceneSwitcher("add-flashcard-view.fxml", "Browser");
    }

    @FXML
    void goToEditTermScene(ActionEvent event) throws IOException {
        Flashcard selectedFlashcard = flashcardsTable.getSelectionModel().getSelectedItem();
        if (selectedFlashcard != null) {
            new SceneSwitcher(selectedFlashcard);

        } else {
            JOptionPane.showMessageDialog(null, "Please select a flashcard to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void returnToHomeScene(ActionEvent event) throws IOException {
        new SceneSwitcher("home-view.fxml", "Home");
    }

    @FXML
    void sortTerms(ActionEvent event) {

    }

    @FXML
    public void loadFlashcardTable() {
        ObservableList<Flashcard> flashcards = FXCollections.observableList(CSVProcessor.loadFlashcards());
        flashcardsTable.setItems(flashcards);
    }

    public void initialize() {
        termColumn.setCellValueFactory(cellData -> cellData.getValue().termProperty());
        definitionColumn.setCellValueFactory(cellData -> cellData.getValue().definitionProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        reviewDateColumn.setCellValueFactory(cellData -> cellData.getValue().reviewDateProperty());

        // loadFlashcardTable is called in SceneSwitcher
    }
}
