package com.vityarthi.voyageai.service;

import com.vityarthi.voyageai.model.Expense;
import com.vityarthi.voyageai.model.Trip;
import com.vityarthi.voyageai.util.FileStorageUtil;

import java.util.*;
import java.util.stream.Collectors;

public class TripService {

    private final List<Trip> trips;
    private final List<Expense> expenses;

    public TripService() {
        this.trips = FileStorageUtil.loadTrips();
        this.expenses = FileStorageUtil.loadExpenses();
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        FileStorageUtil.saveTrips(trips);
    }

    public boolean deleteTrip(String tripId) {
        boolean removed = trips.removeIf(t -> t.getId().equalsIgnoreCase(tripId));
        if (removed) {
            expenses.removeIf(e -> e.getTripId().equalsIgnoreCase(tripId));
            FileStorageUtil.saveTrips(trips);
            FileStorageUtil.saveExpenses(expenses);
        }
        return removed;
    }

    public List<Trip> getAllTrips() {
        return Collections.unmodifiableList(trips);
    }

    public Optional<Trip> getTripById(String tripId) {
        return trips.stream()
                .filter(t -> t.getId().equalsIgnoreCase(tripId))
                .findFirst();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        FileStorageUtil.saveExpenses(expenses);
    }

    public List<Expense> getExpensesForTrip(String tripId) {
        return expenses.stream()
                .filter(e -> e.getTripId().equalsIgnoreCase(tripId))
                .collect(Collectors.toList());
    }

    public double getTotalSpentForTrip(String tripId) {
        return expenses.stream()
                .filter(e -> e.getTripId().equalsIgnoreCase(tripId))
                .mapToDouble(Expense::getAmountUsd)
                .sum();
    }

    public Map<String, Double> getExpensesByCategory(String tripId) {
        return expenses.stream()
                .filter(e -> e.getTripId().equalsIgnoreCase(tripId))
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmountUsd)
                ));
    }

    public String getTripBudgetReport(String tripId) {
        Optional<Trip> tripOpt = getTripById(tripId);
        if (tripOpt.isEmpty()) {
            return "❌ Trip not found with ID: " + tripId;
        }

        Trip trip = tripOpt.get();
        double totalSpent = getTotalSpentForTrip(tripId);
        double budget = trip.getBudgetUsd();
        double remaining = budget - totalSpent;
        double usagePct = (budget > 0) ? (totalSpent / budget) * 100 : 0;

        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("   TRIP BUDGET SUMMARY: ").append(trip.getDestination()).append(" (").append(trip.getCountry()).append(")\n");
        sb.append("=======================================================\n");
        sb.append(String.format("Total Trip Budget : $%.2f USD\n", budget));
        sb.append(String.format("Total Amount Spent: $%.2f USD\n", totalSpent));
        sb.append(String.format("Remaining Balance : $%.2f USD\n", remaining));
        sb.append(String.format("Budget Utilization: %.1f%%\n", usagePct));
        sb.append("Visual Progress   : ").append(generateProgressBar(usagePct)).append("\n");

        sb.append("\n--- Spending Breakdown by Category ---\n");
        Map<String, Double> categoryMap = getExpensesByCategory(tripId);
        if (categoryMap.isEmpty()) {
            sb.append("   (No expenses logged yet for this trip)\n");
        } else {
            categoryMap.forEach((cat, amt) -> 
                sb.append(String.format("   • %-12s: $%.2f USD (%.1f%%)\n", cat, amt, (amt / totalSpent) * 100))
            );
        }
        sb.append("=======================================================\n");
        return sb.toString();
    }

    private String generateProgressBar(double percentage) {
        int totalBlocks = 15;
        int filledBlocks = (int) Math.min(totalBlocks, (percentage / 100.0) * totalBlocks);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filledBlocks) bar.append("=");
            else bar.append("-");
        }
        bar.append("]");
        if (percentage >= 100) {
            bar.append(" (OVER BUDGET!)");
        } else if (percentage >= 80) {
            bar.append(" (WARNING: Near Limit)");
        }
        return bar.toString();
    }
}