package com.vityarthi.voyageai.model;

/**
 * Represents a planned travel trip with budget tracking.
 */
public class Trip {
    private String id;
    private String destination;
    private String country;
    private String startDate; // "YYYY-MM-DD"
    private int durationDays;
    private double budgetUsd;
    private String notes;

    public Trip() {}

    public Trip(String id, String destination, String country, String startDate, int durationDays, double budgetUsd, String notes) {
        this.id = id;
        this.destination = destination;
        this.country = country;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.budgetUsd = budgetUsd;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public double getBudgetUsd() { return budgetUsd; }
    public void setBudgetUsd(double budgetUsd) { this.budgetUsd = budgetUsd; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toCsvRow() {
        return String.join(",", id, sanitize(destination), sanitize(country), startDate, 
                String.valueOf(durationDays), String.valueOf(budgetUsd), sanitize(notes));
    }

    public static Trip fromCsvRow(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 7) return null;
        return new Trip(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            Integer.parseInt(parts[4].trim()),
            Double.parseDouble(parts[5].trim()),
            parts[6].trim()
        );
    }

    private static String sanitize(String text) {
        return text == null ? "" : text.replace(",", ";");
    }

    @Override
    public String toString() {
        return String.format("[%s] %s, %s | %d Days | Starts: %s | Budget: $%.2f",
            id, destination, country, durationDays, startDate, budgetUsd);
    }
}