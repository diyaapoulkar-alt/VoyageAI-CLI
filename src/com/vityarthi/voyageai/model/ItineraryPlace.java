package com.vityarthi.voyageai.model;

/**
 * Represents a recommended attraction/place to visit in an itinerary.
 */
public class ItineraryPlace {
    private String name;
    private String category; // "Sightseeing", "Culinary", "Culture", "Nature", "Shopping"
    private double estimatedCostUsd;
    private String recommendedTime; // "Morning", "Afternoon", "Evening"
    private String description;
    private double rating;

    public ItineraryPlace() {}

    public ItineraryPlace(String name, String category, double estimatedCostUsd, String recommendedTime, String description, double rating) {
        this.name = name;
        this.category = category;
        this.estimatedCostUsd = estimatedCostUsd;
        this.recommendedTime = recommendedTime;
        this.description = description;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getEstimatedCostUsd() { return estimatedCostUsd; }
    public String getRecommendedTime() { return recommendedTime; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }

    public String toJson() {
        return String.format(
            "{\"name\":\"%s\",\"category\":\"%s\",\"estimatedCostUsd\":%.2f,\"recommendedTime\":\"%s\",\"description\":\"%s\",\"rating\":%.1f}",
            escapeJson(name), escapeJson(category), estimatedCostUsd, escapeJson(recommendedTime), escapeJson(description), rating
        );
    }

    private static String escapeJson(String text) {
        return text == null ? "" : text.replace("\"", "\\\"").replace("\n", " ");
    }
}
