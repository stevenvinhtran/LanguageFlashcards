package com.stevenvinhtran.languageflashcards.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVLoader {
    public static ArrayList<Flashcard> loadFlashcards() {
        ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(CSVLoader.class.getResourceAsStream("/com/stevenvinhtran/languageflashcards/flashcards.csv")))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length == 4) {
                    Flashcard flashcard = new Flashcard(values[0], values[1], values[2], values[3]);
                    flashcards.add(flashcard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flashcards;
    }
}
