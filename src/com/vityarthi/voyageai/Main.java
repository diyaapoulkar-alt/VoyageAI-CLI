package com.vityarthi.voyageai;

import com.vityarthi.voyageai.model.Expense;
import com.vityarthi.voyageai.model.Trip;
import com.vityarthi.voyageai.model.WeatherData;
import com.vityarthi.voyageai.service.GeminiAiService;
import com.vityarthi.voyageai.service.TripService;
import com.vityarthi.voyageai.service.WeatherService;
import com.vityarthi.voyageai.util.InputValidator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TripService tripService = new TripService();
    private static final WeatherService weatherService = new WeatherService();
    private static final GeminiAiService geminiAiService = new GeminiAiService();
    private static String geminiApiKey = "";

    public static void main(String[] args) {
        printHeader();

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Enter your choice (1-9): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    createNewTripFlow();
                    break;
                case "2":
                    viewAllTripsFlow();
                    break;
                case "3":
                    logTripExpenseFlow();
                    break;
                case "4":
                    viewTripBudgetReportFlow();
                    break;
                case "5":
                    checkDestinationWeatherFlow();
                    break;
                case "6":
                    generateAiItineraryFlow();
                    break;
                case "7":
                    generatePackingListFlow();
                    break;
                case "8":
                    runSystemTestsFlow();
                    break;
                case "9":
                    exit = true;
                    System.out.println("\nThank you for using VoyageAI Travel Planner. Bon Voyage! ✈️");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please enter a number between 1 and 9.");
            }
            if (!exit) {
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
            }
        }
    }

    private static void printHeader() {
        System.out.println("==========================================================================");
        System.out.println("   ✈️ VoyageAI CLI - AI Smart Travel Planner & Weather Intelligence       ");
        System.out.println("==========================================================================");
        System.out.println("   Status: Active | Weather API: Open-Meteo | AI Engine: Google Gemini API ");
        System.out.println("==========================================================================");
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. 🗺️ Create New Trip Plan");
        System.out.println("2. 📋 View All Saved Trips");
        System.out.println("3. 💸 Log Trip Expense (Flight, Hotel, Food, Activity)");
        System.out.println("4. 📊 View Trip Budget & Category Expenses");
        System.out.println("5. 🌤️ Check Real-Time Weather (Open-Meteo API)");
        System.out.println("6. 🤖 Generate AI Day-by-Day Itinerary (Gemini AI)");
        System.out.println("7. 📦 Generate Smart Weather Packing Checklist");
        System.out.println("8. 🧪 Run Automated System Unit Tests");
        System.out.println("9. 🚪 Exit");
        System.out.println("--------------------------------------------------");
    }

    private static void createNewTripFlow() {
        System.out.println("\n--- 🗺️ Create New Trip Plan ---");

        System.out.print("Enter Destination City (e.g., Paris, Tokyo, New York, London): ");
        String destination = scanner.nextLine().trim();
        if (!InputValidator.isNonEmpty(destination)) {
            System.out.println("❌ Destination city cannot be empty.");
            return;
        }

        System.out.print("Enter Country: ");
        String country = scanner.nextLine().trim();
        if (!InputValidator.isNonEmpty(country)) country = "Global";

        System.out.print("Enter Trip Start Date (YYYY-MM-DD) [Default Today]: ");
        String startDate = scanner.nextLine().trim();
        if (startDate.isEmpty()) startDate = LocalDate.now().toString();
        if (!InputValidator.isValidDate(startDate)) {
            System.out.println("❌ Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        System.out.print("Enter Duration (in days): ");
        int duration;
        try {
            duration = Integer.parseInt(scanner.nextLine().trim());
            if (!InputValidator.isValidPositiveInt(duration)) {
                System.out.println("❌ Duration must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number.");
            return;
        }

        System.out.print("Enter Total Trip Budget ($ USD): ");
        double budget;
        try {
            budget = Double.parseDouble(scanner.nextLine().trim());
            if (!InputValidator.isValidPositiveDouble(budget)) {
                System.out.println("❌ Budget must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid numeric budget.");
            return;
        }

        System.out.print("Enter Trip Notes / Purpose (optional): ");
        String notes = scanner.nextLine().trim();

        String tripId = "TRIP_" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Trip trip = new Trip(tripId, destination, country, startDate, duration, budget, notes);
        tripService.addTrip(trip);

        System.out.println("✅ Trip plan created successfully! Trip ID: " + tripId);

        System.out.println("\nFetching live weather forecast for " + destination + "...");
        WeatherData weather = weatherService.getWeatherForCity(destination);
        System.out.println("🌤️ " + weather.toString());
    }

    private static void viewAllTripsFlow() {
        System.out.println("\n--- 📋 All Saved Trips ---");
        List<Trip> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            System.out.println("No trips saved yet. Use Menu Option 1 to create a trip.");
            return;
        }

        for (Trip t : trips) {
            double totalSpent = tripService.getTotalSpentForTrip(t.getId());
            System.out.printf("[%s] %s, %s | %d Days | Starts: %s | Budget: $%.2f | Spent: $%.2f\n",
                    t.getId(), t.getDestination(), t.getCountry(), t.getDurationDays(),
                    t.getStartDate(), t.getBudgetUsd(), totalSpent);
        }
    }

    private static void logTripExpenseFlow() {
        System.out.println("\n--- 💸 Log Trip Expense ---");
        List<Trip> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            System.out.println("❌ No active trips found. Please create a trip first.");
            return;
        }

        System.out.println("Select a Trip ID from below:");
        for (Trip t : trips) {
            System.out.println("  • ID: " + t.getId() + " (" + t.getDestination() + ")");
        }

        System.out.print("Enter Trip ID: ");
        String tripId = scanner.nextLine().trim().toUpperCase();
        Optional<Trip> tripOpt = tripService.getTripById(tripId);
        if (tripOpt.isEmpty()) {
            System.out.println("❌ Invalid Trip ID.");
            return;
        }

        System.out.print("Enter Category (Flight, Hotel, Food, Activity, Transport, Misc): ");
        String category = scanner.nextLine().trim();
        if (!InputValidator.isValidCategory(category)) {
            System.out.println("❌ Invalid category. Choose from Flight, Hotel, Food, Activity, Transport, Misc.");
            return;
        }

        System.out.print("Enter Amount ($ USD): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
            if (!InputValidator.isValidPositiveDouble(amount)) {
                System.out.println("❌ Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number.");
            return;
        }

        System.out.print("Enter Date (YYYY-MM-DD) [Default Today]: ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()) date = LocalDate.now().toString();

        System.out.print("Enter Short Description: ");
        String desc = scanner.nextLine().trim();

        String expId = "EXP_" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Expense expense = new Expense(expId, tripId, category, amount, date, desc);
        tripService.addExpense(expense);

        System.out.println("✅ Expense logged successfully! Expense ID: " + expId);
    }

    private static void viewTripBudgetReportFlow() {
        System.out.println("\n--- 📊 Trip Budget & Category Report ---");
        System.out.print("Enter Trip ID: ");
        String tripId = scanner.nextLine().trim().toUpperCase();
        System.out.println(tripService.getTripBudgetReport(tripId));
    }

    private static void checkDestinationWeatherFlow() {
        System.out.println("\n--- 🌤️ Check Real-Time Weather ---");
        System.out.print("Enter City Name (e.g., Tokyo, Paris, Rome, Sydney): ");
        String city = scanner.nextLine().trim();
        if (!InputValidator.isNonEmpty(city)) {
            System.out.println("❌ City name cannot be empty.");
            return;
        }

        System.out.println("Connecting to Open-Meteo REST API...");
        WeatherData weather = weatherService.getWeatherForCity(city);
        System.out.println("\n" + weather.toString());
    }

    private static void generateAiItineraryFlow() {
        System.out.println("\n--- 🤖 Gemini AI Day-by-Day Itinerary ---");
        System.out.print("Enter Destination City: ");
        String destination = scanner.nextLine().trim();
        
        System.out.print("Enter Duration (Days): ");
        int duration = 3;
        try { duration = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) {}

        System.out.print("Enter Budget ($ USD): ");
        double budget = 1500;
        try { budget = Double.parseDouble(scanner.nextLine().trim()); } catch (Exception e) {}

        if (geminiApiKey.isEmpty()) {
            System.out.print("Enter your Google Gemini API Key (Leave blank for offline AI mode): ");
            geminiApiKey = scanner.nextLine().trim();
        }

        System.out.println("\n1. Fetching live weather for " + destination + "...");
        WeatherData weather = weatherService.getWeatherForCity(destination);

        System.out.println("2. Querying Gemini AI Engine...");
        String weatherSummary = String.format("%.1f°C, %s", weather.getTemperatureC(), weather.getConditionSummary());
        String itinerary = geminiAiService.generateItinerary(destination, duration, budget, weatherSummary, geminiApiKey);
        
        System.out.println("\n" + itinerary);

        System.out.print("\nDo you want to export this itinerary to a text file? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            String filename = "itinerary_" + destination.toLowerCase().replace(" ", "_") + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println(itinerary);
                System.out.println("✅ Itinerary exported to: " + filename);
            } catch (IOException e) {
                System.out.println("❌ Error saving file: " + e.getMessage());
            }
        }
    }

    private static void generatePackingListFlow() {
        System.out.println("\n--- 📦 Generate Weather-Smart Packing Checklist ---");
        System.out.print("Enter Destination City: ");
        String destination = scanner.nextLine().trim();

        WeatherData weather = weatherService.getWeatherForCity(destination);
        String weatherSummary = String.format("%.1f°C, %s", weather.getTemperatureC(), weather.getConditionSummary());
        System.out.println("Weather Summary: " + weatherSummary);

        String packingList = geminiAiService.generatePackingList(destination, 5, weatherSummary, geminiApiKey);
        System.out.println("\n" + packingList);
    }

    private static void runSystemTestsFlow() {
        System.out.println();
        VoyageAiTest.main(new String[]{});
    }
}

