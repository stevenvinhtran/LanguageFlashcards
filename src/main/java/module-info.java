module com.example.languageflashcards {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.stevenvinhtran.languageflashcards to javafx.fxml;
    exports com.stevenvinhtran.languageflashcards;
    exports com.stevenvinhtran.languageflashcards.Controller;
    opens com.stevenvinhtran.languageflashcards.Controller to javafx.fxml;
}