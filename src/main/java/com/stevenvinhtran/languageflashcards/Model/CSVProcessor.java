package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CSVProcessor {

    private static final Path FLASHCARD_CSV_PATH = resolvePath("data/flashcards.csv");
    private static final Path SETTINGS_CSV_PATH = resolvePath("data/settings.csv");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<String> flashcardsHeader = Arrays.asList("term,definition,type,reviewDate,dateAdded,repetitions,easeFactor,interval,isNewCard");
    private static final List<String> settingsHeader = Arrays.asList("newVocabCardsPerDay,newGrammarCardsPerDay,learningSteps,relearningSteps");

    // Public Methods

    public static ArrayList<String> loadSettings() {
        ArrayList<String> settings = new ArrayList<>();

        try {
            List<String> lines = readCSV(SETTINGS_CSV_PATH, settingsHeader);

            boolean skipHeader = true;
            for (String line : lines) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                settings.addAll(parseCSVLine(line));
            }

            if (settings.isEmpty()) {
                applyAndSaveDefaultSettings(settings, "settings.csv is empty\nDefault Settings applied and saved.");
            }
        } catch (IOException e) {
            applyAndSaveDefaultSettings(settings, "Failed to load settings\nDefault settings applied\n" + e.getMessage());
            e.printStackTrace();
        }
        return settings;
    }

    public static void saveSettings(int newVocabCardsPerDay,
                                    int newGrammarCardsPerDay,
                                    int[] learningSteps,
                                    int[] relearningSteps) {
        try {
            Files.createDirectories(SETTINGS_CSV_PATH.getParent());

            String learningStepsStr = Arrays.stream(learningSteps)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
            String relearningStepsStr = Arrays.stream(relearningSteps)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));

            String csvLine = String.format("%d,%d,\"%s\",\"%s\"",
                    newVocabCardsPerDay,
                    newGrammarCardsPerDay,
                    learningStepsStr,
                    relearningStepsStr);

            List<String> lines = Arrays.asList(
                    "newVocabCardsPerDay,newGrammarCardsPerDay,learningSteps,relearningSteps",
                    csvLine
            );

            writeCSV(SETTINGS_CSV_PATH, lines);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save settings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static ArrayList<Flashcard> loadFlashcards() {
        ArrayList<Flashcard> flashcards = new ArrayList<>();

        try {
            List<String> lines = readCSV(FLASHCARD_CSV_PATH, flashcardsHeader);

            boolean skipHeader = true;
            for (String line : lines) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 9) {
                    LocalDateTime reviewDate = LocalDateTime.parse(values[3], formatter);
                    LocalDateTime dateAdded = LocalDateTime.parse(values[4], formatter);
                    int repetitions = Integer.parseInt(values[5]);
                    double easeFactor = Double.parseDouble(values[6]);
                    int interval = Integer.parseInt(values[7]);
                    boolean isNewCard = Boolean.parseBoolean(values[8]);

                    flashcards.add(new Flashcard(
                            values[0], values[1], values[2],
                            reviewDate, dateAdded,
                            repetitions, easeFactor, interval, isNewCard
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flashcards;
    }

    public static void updateFlashcard(Flashcard old, Flashcard updated) {
        List<String> lines = new ArrayList<>();
        boolean hasDuplicate = false;

        try {
            List<String> csvLines = readCSV(FLASHCARD_CSV_PATH, flashcardsHeader);
            boolean isHeader = true;

            for (String line : csvLines) {
                if (isHeader) {
                    lines.add(line);
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 8 && values[0].equals(updated.getTerm()) && !updated.getTerm().equals(old.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!hasDuplicate && values.length >= 8 &&
                        values[0].equals(old.getTerm()) &&
                        values[1].equals(old.getDefinition()) &&
                        values[2].equals(old.getType())) {
                    lines.add(createCSVLine(updated));
                } else {
                    lines.add(line);
                }
            }

            writeCSV(FLASHCARD_CSV_PATH, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateDeck(List<Flashcard> oldDeck, List<Flashcard> newDeck) {
        List<String> lines = new ArrayList<>();

        try {
            List<String> csvLines = readCSV(FLASHCARD_CSV_PATH, flashcardsHeader);
            if (!csvLines.isEmpty()) {
                lines.add(csvLines.get(0));
            }

            for (int i = 1; i < csvLines.size(); i++) {
                String[] values = csvLines.get(i).split(",");
                if (values.length >= 9) {
                    Flashcard matchingOldCard = findMatchingFlashcard(values, (ArrayList<Flashcard>) oldDeck);
                    if (matchingOldCard != null) {
                        Flashcard updatedCard = findUpdatedCard(matchingOldCard, (ArrayList<Flashcard>) newDeck);
                        if (updatedCard != null) {
                            lines.add(createCSVLine(updatedCard));
                            newDeck.remove(updatedCard);
                            continue;
                        }
                    }
                }
                lines.add(csvLines.get(i));
            }

            for (Flashcard newCard : newDeck) {
                lines.add(createCSVLine(newCard));
            }

            writeCSV(FLASHCARD_CSV_PATH, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFlashcard(Flashcard flashcard) {
        List<String> lines = new ArrayList<>();
        boolean hasDuplicate = false;

        try {
            List<String> csvLines = readCSV(FLASHCARD_CSV_PATH, flashcardsHeader);
            boolean isHeader = true;

            for (String line : csvLines) {
                if (isHeader) {
                    lines.add(line);
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 8 && values[0].equals(flashcard.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    lines.add(line);
                }
            }

            if (!hasDuplicate) {
                lines.add(createCSVLine(flashcard));
            }

            writeCSV(FLASHCARD_CSV_PATH, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFlashcard(Flashcard flashcard) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this term?", "Delete Term", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            List<String> lines = new ArrayList<>();

            try {
                List<String> csvLines = readCSV(FLASHCARD_CSV_PATH, flashcardsHeader);
                boolean isHeader = true;

                for (String line : csvLines) {
                    if (isHeader) {
                        lines.add(line);
                        isHeader = false;
                        continue;
                    }

                    String[] values = line.split(",");
                    if (!(values.length >= 8 &&
                            values[0].equals(flashcard.getTerm()) &&
                            values[1].equals(flashcard.getDefinition()) &&
                            values[2].equals(flashcard.getType()))) {
                        lines.add(line);
                    }
                }

                writeCSV(FLASHCARD_CSV_PATH, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Helper Methods

    private static Path resolvePath(String relativePath) {
        return Paths.get(System.getProperty("user.dir")).resolve(relativePath);
    }

    private static List<String> readCSV(Path path, List<String> headerIfMissing) throws IOException {
        if (!Files.exists(path) || Files.size(path) == 0) {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            JOptionPane.showMessageDialog(null, path + " not found or exists but has no header. Default will be created with header.", "Error", JOptionPane.ERROR_MESSAGE);
            Files.write(path, headerIfMissing);
        }
        return Files.readAllLines(path);
    }


    private static void writeCSV(Path path, List<String> lines) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, lines);
    }

    private static List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());
        return values;
    }

    private static void applyAndSaveDefaultSettings(List<String> settings, String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        settings.addAll(Arrays.asList("20", "10", "30,1440,4320", "30,1440"));
        saveSettings(20, 10, new int[]{30, 1440, 4320}, new int[]{30, 1440});
    }

    private static Flashcard findMatchingFlashcard(String[] values, ArrayList<Flashcard> deck) {
        return deck.stream()
                .filter(card -> card.getTerm().equals(values[0]) &&
                        card.getDefinition().equals(values[1]) &&
                        card.getType().equals(values[2]))
                .findFirst()
                .orElse(null);
    }

    private static Flashcard findUpdatedCard(Flashcard oldCard, ArrayList<Flashcard> newDeck) {
        return newDeck.stream()
                .filter(card -> card.getTerm().equals(oldCard.getTerm()) &&
                        card.getDefinition().equals(oldCard.getDefinition()) &&
                        card.getType().equals(oldCard.getType()))
                .findFirst()
                .orElse(null);
    }

    private static String createCSVLine(Flashcard card) {
        return String.join(",",
                card.getTerm(),
                card.getDefinition(),
                card.getType(),
                card.getReviewDate().format(formatter),
                card.getDateAdded().format(formatter),
                String.valueOf(card.getRepetitions()),
                String.valueOf(card.getEaseFactor()),
                String.valueOf(card.getInterval()),
                String.valueOf(card.getIsNewCard())
        );
    }
}
