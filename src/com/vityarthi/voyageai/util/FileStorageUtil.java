package com.vityarthi.voyageai.util;

import com.vityarthi.voyageai.model.Expense;
import com.vityarthi.voyageai.model.Trip;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageUtil {

    private static final String DATA_DIR = "data";
    private static final String TRIPS_FILE = DATA_DIR + "/trips.csv";
    private static final String EXPENSES_FILE = DATA_DIR + "/expenses.csv";

    public static void ensureDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create data directory: " + e.getMessage());
        }
    }

    public static void saveTrips(List<Trip> trips) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRIPS_FILE))) {
            for (Trip t : trips) {
                writer.println(t.toCsvRow());
            }
        } catch (IOException e) {
            System.err.println("Error saving trips: " + e.getMessage());
        }
    }

    public static List<Trip> loadTrips() {
        ensureDataDirectory();
        List<Trip> list = new ArrayList<>();
        File file = new File(TRIPS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Trip t = Trip.fromCsvRow(line);
                    if (t != null) list.add(t);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading trips: " + e.getMessage());
        }
        return list;
    }

    public static void saveExpenses(List<Expense> expenses) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSES_FILE))) {
            for (Expense e : expenses) {
                writer.println(e.toCsvRow());
            }
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    public static List<Expense> loadExpenses() {
        ensureDataDirectory();
        List<Expense> list = new ArrayList<>();
        File file = new File(EXPENSES_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Expense e = Expense.fromCsvRow(line);
                    if (e != null) list.add(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
        return list;
    }
}