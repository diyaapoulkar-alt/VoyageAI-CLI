package com.vityarthi.voyageai.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputValidator {

    public static boolean isNonEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isValidPositiveDouble(double amount) {
        return amount > 0;
    }

    public static boolean isValidPositiveInt(int value) {
        return value > 0;
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return false;
        try {
            LocalDate.parse(dateStr.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidCategory(String category) {
        if (category == null) return false;
        String cat = category.trim();
        return cat.equalsIgnoreCase("Flight") || cat.equalsIgnoreCase("Hotel") ||
               cat.equalsIgnoreCase("Food") || cat.equalsIgnoreCase("Activity") ||
               cat.equalsIgnoreCase("Transport") || cat.equalsIgnoreCase("Misc");
    }
}
