package com.vityarthi.voyageai;

import com.vityarthi.voyageai.model.Expense;
import com.vityarthi.voyageai.model.Trip;
import com.vityarthi.voyageai.model.WeatherData;
import com.vityarthi.voyageai.service.TripService;
import com.vityarthi.voyageai.service.WeatherService;
import com.vityarthi.voyageai.util.InputValidator;

public class VoyageAiTest {

    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     RUNNING VOYAGEAI AUTOMATED UNIT TESTS        ");
        System.out.println("==================================================");

        testTripModelAndCsv();
        testExpenseModelAndCsv();
        testInputValidation();
        testWeatherServiceFallback();
        testTripServiceBudgetCalculation();

        System.out.println("\n--------------------------------------------------");
        System.out.printf("Test Execution Summary: %d / %d Tests Passed.\n", passedTests, totalTests);
        if (passedTests == totalTests) {
            System.out.println("STATUS: ALL TESTS PASSED SUCCESSFULLY! ✅");
        } else {
            System.out.println("STATUS: SOME TESTS FAILED ❌");
        }
        System.out.println("--------------------------------------------------");
    }

    private static void assertEquals(double expected, double actual, String testName) {
        totalTests++;
        if (Math.abs(expected - actual) < 0.001) {
            passedTests++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.printf("[FAIL] %s - Expected: %.2f, Got: %.2f\n", testName, expected, actual);
        }
    }

    private static void assertTrue(boolean condition, String testName) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }

    private static void testTripModelAndCsv() {
        Trip trip = new Trip("TRIP101", "Tokyo", "Japan", "2026-10-01", 7, 2500.0, "Autumn trip");
        assertTrue("TRIP101".equals(trip.getId()), "Trip ID getter test");
        assertEquals(2500.0, trip.getBudgetUsd(), "Trip budget getter test");

        String csv = trip.toCsvRow();
        Trip restored = Trip.fromCsvRow(csv);
        assertTrue(restored != null && "Tokyo".equals(restored.getDestination()), "Trip CSV Serialization & Parsing test");
    }

    private static void testExpenseModelAndCsv() {
        Expense expense = new Expense("EXP101", "TRIP101", "Hotel", 450.0, "2026-10-02", "Shinjuku Hotel 2 nights");
        assertEquals(450.0, expense.getAmountUsd(), "Expense amount getter test");
        
        String csv = expense.toCsvRow();
        Expense restored = Expense.fromCsvRow(csv);
        assertTrue(restored != null && "Hotel".equals(restored.getCategory()), "Expense CSV Serialization & Parsing test");
    }

    private static void testInputValidation() {
        assertTrue(InputValidator.isValidPositiveDouble(150.0), "Valid positive double test");
        assertTrue(!InputValidator.isValidPositiveDouble(-20.0), "Invalid negative double test");
        assertTrue(InputValidator.isValidDate("2026-12-25"), "Valid ISO date format test");
        assertTrue(!InputValidator.isValidDate("2026/12/25"), "Invalid date format test");
        assertTrue(InputValidator.isValidCategory("Flight"), "Valid expense category test");
        assertTrue(!InputValidator.isValidCategory("InvalidCat"), "Invalid expense category test");
    }

    private static void testWeatherServiceFallback() {
        WeatherService ws = new WeatherService();
        WeatherData wd = ws.getFallbackWeather("Paris");
        assertTrue(wd != null && wd.getTemperatureC() > 0, "WeatherService fallback weather object generation test");
    }

    private static void testTripServiceBudgetCalculation() {
        TripService ts = new TripService();
        String testId = "TEST_TRIP_999";
        ts.addTrip(new Trip(testId, "Rome", "Italy", "2026-11-01", 5, 1000.0, "Test trip"));
        
        ts.addExpense(new Expense("E1", testId, "Food", 200.0, "2026-11-01", "Dinner"));
        ts.addExpense(new Expense("E2", testId, "Hotel", 300.0, "2026-11-01", "Hotel deposit"));

        double totalSpent = ts.getTotalSpentForTrip(testId);
        assertEquals(500.0, totalSpent, "TripService total spent calculation test");

        ts.deleteTrip(testId);
    }
}
