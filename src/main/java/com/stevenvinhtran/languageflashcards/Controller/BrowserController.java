package com.stevenvinhtran.languageflashcards.Controller;

import com.stevenvinhtran.languageflashcards.Model.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BrowserController {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private AnchorPane browserAnchorPane;

    @FXML
    private TableColumn<?, ?> definitionColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<?, ?> reviewDateColumn;

    @FXML
    private Label sortByLabel;

    @FXML
    private SplitMenuButton sortByMenu;

    @FXML
    private TableColumn<?, ?> termColumn;

    @FXML
    private TableView<?> flashcardsTable;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    void deleteTerm(ActionEvent event) {

    }

    @FXML
    void goToAddTermScene(ActionEvent event) {

    }

    @FXML
    void goToEditTermScene(ActionEvent event) {

    }

    @FXML
    void returnToHomeScene(ActionEvent event) throws IOException {
        new SceneSwitcher("home-view.fxml", "Home");
    }

    @FXML
    void sortTerms(ActionEvent event) {

    }

}
