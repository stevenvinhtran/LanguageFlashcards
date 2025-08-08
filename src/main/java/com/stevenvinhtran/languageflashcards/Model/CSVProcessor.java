package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CSVProcessor {

    private static final Path FLASHCARD_CSV_PATH = resolvePath("data/flashcards.csv");
    private static final Path SETTINGS_CSV_PATH = resolvePath("data/settings.csv");
    private static final Path DAILY_COUNTS_CSV_PATH = resolvePath("data/daily_counts.csv");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<String> flashcardsHeader = Arrays.asList("term,definition,type,reviewDate,dateAdded,repetitions,easeFactor,interval,isNewCard,isRelearning");
    private static final List<String> settingsHeader = Arrays.asList("newVocabCardsPerDay,newGrammarCardsPerDay,learningSteps,relearningSteps");
    private static final List<String> dailyCountsHeader = Arrays.asList("date,numOfNewVocabCards,numOfNewGrammarCards");

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

                List<String> values = parseCSVLine(line);
                if (values.size() >= 9) {
                    LocalDateTime reviewDate = LocalDateTime.parse(values.get(3), formatter);
                    LocalDateTime dateAdded = LocalDateTime.parse(values.get(4), formatter);
                    int repetitions = Integer.parseInt(values.get(5));
                    double easeFactor = Double.parseDouble(values.get(6));
                    int interval = Integer.parseInt(values.get(7));
                    boolean isNewCard = Boolean.parseBoolean(values.get(8));
                    boolean isRelearning = Boolean.parseBoolean(values.get(9));

                    flashcards.add(new Flashcard(
                            values.get(0), values.get(1), values.get(2),
                            reviewDate, dateAdded,
                            repetitions, easeFactor, interval, isNewCard, isRelearning
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

                List<String> values = parseCSVLine(line);
                if (values.size() >= 9 && values.get(0).equals(updated.getTerm()) && !updated.getTerm().equals(old.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!hasDuplicate && values.size() >= 9 &&
                        values.get(0).equals(old.getTerm()) &&
                        values.get(1).equals(old.getDefinition()) &&
                        values.get(2).equals(old.getType())) {
                    lines.add(createFlashcardCSVLine(updated));
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
                List<String> values = parseCSVLine(csvLines.get(i));
                if (values.size() >= 9) {
                    Flashcard matchingOldCard = findMatchingFlashcard(values, (ArrayList<Flashcard>) oldDeck);
                    if (matchingOldCard != null) {
                        Flashcard updatedCard = findUpdatedCard(matchingOldCard, (ArrayList<Flashcard>) newDeck);
                        if (updatedCard != null) {
                            lines.add(createFlashcardCSVLine(updatedCard));
                            newDeck.remove(updatedCard);
                            continue;
                        }
                    }
                }
                lines.add(csvLines.get(i));
            }

            for (Flashcard newCard : newDeck) {
                lines.add(createFlashcardCSVLine(newCard));
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

                List<String> values = parseCSVLine(line);
                if (values.size() >= 9 && values.get(1).equals(flashcard.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    lines.add(line);
                }
            }

            if (!hasDuplicate) {
                lines.add(createFlashcardCSVLine(flashcard));
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

                    List<String> values = parseCSVLine(line);
                    if (!(values.size() >= 9 &&
                            values.get(0).equals(flashcard.getTerm()) &&
                            values.get(1).equals(flashcard.getDefinition()) &&
                            values.get(2).equals(flashcard.getType()))) {
                        lines.add(line);
                    }
                }

                writeCSV(FLASHCARD_CSV_PATH, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void incrementDailyNewCardCount(String cardType) {
        try {
            LocalDate today = LocalDate.now();
            List<String> lines = readCSV(DAILY_COUNTS_CSV_PATH, dailyCountsHeader);

            List<String> newLines = new ArrayList<>();
            newLines.add(dailyCountsHeader.get(0)); // Add header

            boolean foundToday = false;

            // For loop in case daily_counts.csv has multiple entries
            for (int i = 1; i < lines.size(); i++) {
                List<String> values = parseCSVLine(lines.get(i));
                if (values.size() >= 3 && values.get(0).equals(today.toString())) {
                    foundToday = true;
                    int vocabCount = Integer.parseInt(values.get(1));
                    int grammarCount = Integer.parseInt(values.get(2));

                    if (cardType.equals("Vocabulary")) {
                        vocabCount++;
                    } else if (cardType.equals("Grammar")) {
                        grammarCount++;
                    }

                    newLines.add(String.format("%s,%d,%d", today, vocabCount, grammarCount));
                }
                // Skip all other dates
            }

            // If today's entry wasn't found, create a new one
            if (!foundToday) {
                int vocabCount = cardType.equals("Vocabulary") ? 1 : 0;
                int grammarCount = cardType.equals("Grammar") ? 1 : 0;
                newLines.add(String.format("%s,%d,%d", today, vocabCount, grammarCount));
            }

            writeCSV(DAILY_COUNTS_CSV_PATH, newLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getTodaysNewCardCounts() {
        try {
            LocalDate today = LocalDate.now();
            List<String> lines = readCSV(DAILY_COUNTS_CSV_PATH, dailyCountsHeader);

            // Clean up old entries while we're here
            List<String> newLines = new ArrayList<>();
            newLines.add(dailyCountsHeader.get(0));

            int[] counts = new int[]{0, 0};
            boolean foundToday = false;

            for (int i = 1; i < lines.size(); i++) {
                List<String> values = parseCSVLine(lines.get(i));
                if (values.size() >= 3 && values.get(0).equals(today.toString())) {
                    foundToday = true;
                    counts[0] = Integer.parseInt(values.get(1));
                    counts[1] = Integer.parseInt(values.get(2));
                    newLines.add(lines.get(i));
                }
                // Other dates are skipped
            }

            // If we modified the file, write it back
            if (lines.size() != newLines.size()) {
                writeCSV(DAILY_COUNTS_CSV_PATH, newLines);
            }

            return counts;
        } catch (IOException e) {
            e.printStackTrace();
            return new int[]{0, 0};
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

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    currentValue.append('\"'); // Escaped quote
                    i++;
                } else {
                    inQuotes = !inQuotes; // Toggle state
                }
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
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

    private static Flashcard findMatchingFlashcard(List<String> values, ArrayList<Flashcard> deck) {
        return deck.stream()
                .filter(card -> card.getTerm().equals(values.get(0)) &&
                        card.getDefinition().equals(values.get(1)) &&
                        card.getType().equals(values.get(2)))
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

    private static String escapeCSVField(String field) {
        if (field.contains("\"")) {
            field = field.replace("\"", "\"\""); // escape quotes
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = "\"" + field + "\""; // wrap in quotes if needed
        }
        return field;
    }

    private static String createFlashcardCSVLine(Flashcard card) {
        return String.join(",",
                escapeCSVField(card.getTerm()),
                escapeCSVField(card.getDefinition()),
                escapeCSVField(card.getType()),
                escapeCSVField(card.getReviewDate().format(formatter)),
                escapeCSVField(card.getDateAdded().format(formatter)),
                escapeCSVField(String.valueOf(card.getRepetitions())),
                escapeCSVField(String.valueOf(card.getEaseFactor())),
                escapeCSVField(String.valueOf(card.getInterval())),
                escapeCSVField(String.valueOf(card.getIsNewCard())),
                escapeCSVField(String.valueOf(card.getIsRelearning()))
        );
    }

}
