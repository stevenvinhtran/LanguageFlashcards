package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {
    private static final Path CSV_PATH = Paths.get("data/flashcards.csv");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ArrayList<Flashcard> loadFlashcards() {
        ArrayList<Flashcard> flashcards = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
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

                    Flashcard flashcard = new Flashcard(
                            values[0], values[1], values[2],
                            reviewDate, dateAdded,
                            repetitions, easeFactor, interval, isNewCard
                    );
                    flashcards.add(flashcard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flashcards;
    }

    public static void updateFlashcard(Flashcard old, Flashcard updated) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
            String line;
            boolean isHeader = true;
            boolean hasDuplicate = false;

            while ((line = br.readLine()) != null) {
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
                } else {
                    if (!hasDuplicate && values.length >= 8 &&
                            values[0].equals(old.getTerm()) &&
                            values[1].equals(old.getDefinition()) &&
                            values[2].equals(old.getType())) {
                        lines.add(String.join(",",
                                updated.getTerm(),
                                updated.getDefinition(),
                                updated.getType(),
                                updated.getReviewDate().format(formatter),
                                updated.getDateAdded().format(formatter),
                                String.valueOf(updated.getRepetitions()),
                                String.valueOf(updated.getEaseFactor()),
                                String.valueOf(updated.getInterval()),
                                String.valueOf(updated.getIsNewCard())
                        ));
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH)) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFlashcard(Flashcard flashcard) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
            String line;
            boolean isHeader = true;
            boolean hasDuplicate = false;

            while ((line = br.readLine()) != null) {
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
                lines.add(String.join(",",
                        flashcard.getTerm(),
                        flashcard.getDefinition(),
                        flashcard.getType(),
                        flashcard.getReviewDate().format(formatter),
                        flashcard.getDateAdded().format(formatter),
                        String.valueOf(flashcard.getRepetitions()),
                        String.valueOf(flashcard.getEaseFactor()),
                        String.valueOf(flashcard.getInterval()),
                        String.valueOf(flashcard.getIsNewCard())
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH)) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFlashcard(Flashcard flashcard) {
        List<String> lines = new ArrayList<>();

        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this term?", "Delete Term", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {
                    if (isHeader) {
                        lines.add(line);
                        isHeader = false;
                        continue;
                    }

                    String[] values = line.split(",");
                    if (values.length >= 8 &&
                            values[0].equals(flashcard.getTerm()) &&
                            values[1].equals(flashcard.getDefinition()) &&
                            values[2].equals(flashcard.getType())) {
                        // Skip this line (don't add to lines)
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH)) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}