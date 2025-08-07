package com.stevenvinhtran.languageflashcards.Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CSVProcessor {
    private static final Path CSV_PATH = Paths.get("data/flashcards.csv");

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
                if (values.length == 4 && values[0].equals(updated.getTerm()) && !updated.getTerm().equals(old.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (!hasDuplicate && values.length == 4 &&
                            values[0].equals(old.getTerm()) &&
                            values[1].equals(old.getDefinition()) &&
                            values[2].equals(old.getType()) &&
                            values[3].equals(old.getReviewDate())) {
                        lines.add(String.join(",", updated.getTerm(), updated.getDefinition(), updated.getType(), updated.getReviewDate()));
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
                if (values.length == 4 && values[0].equals(flashcard.getTerm())) {
                    hasDuplicate = true;
                    lines.add(line);
                    JOptionPane.showMessageDialog(null, "Duplicate Term!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    lines.add(line);
                }
            }
            if (!hasDuplicate) {
                lines.add(String.join(",", flashcard.getTerm(), flashcard.getDefinition(), flashcard.getType(), flashcard.getReviewDate()));
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
                if (values.length == 4 &&
                        values[0].equals(flashcard.getTerm()) &&
                        values[1].equals(flashcard.getDefinition()) &&
                        values[2].equals(flashcard.getType()) &&
                        values[3].equals(flashcard.getReviewDate())) { }
                else {
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
