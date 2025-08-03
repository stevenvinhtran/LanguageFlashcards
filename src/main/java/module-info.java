module com.example.languageflashcards {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.languageflashcards to javafx.fxml;
    exports com.example.languageflashcards;
}