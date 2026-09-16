package com.vityarthi.voyageai.model;

/**
 * Represents an individual expense item linked to a specific trip.
 */
public class Expense {
    private String id;
    private String tripId;
    private String category; // "Flight", "Hotel", "Food", "Activity", "Transport", "Misc"
    private double amountUsd;
    private String date; // "YYYY-MM-DD"
    private String description;

    public Expense() {}

    public Expense(String id, String tripId, String category, double amountUsd, String date, String description) {
        this.id = id;
        this.tripId = tripId;
        this.category = category;
        this.amountUsd = amountUsd;
        this.date = date;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmountUsd() { return amountUsd; }
    public void setAmountUsd(double amountUsd) { this.amountUsd = amountUsd; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String toCsvRow() {
        return String.join(",", id, tripId, category, String.valueOf(amountUsd), date, sanitize(description));
    }

    public static Expense fromCsvRow(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) return null;
        return new Expense(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            Double.parseDouble(parts[3].trim()),
            parts[4].trim(),
            parts[5].trim()
        );
    }

    private static String sanitize(String text) {
        return text == null ? "" : text.replace(",", ";");
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s | $%-8.2f | %s | %s",
            id, category, amountUsd, date, description);
    }
}