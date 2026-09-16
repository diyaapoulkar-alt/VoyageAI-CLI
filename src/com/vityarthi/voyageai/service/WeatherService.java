package com.vityarthi.voyageai.service;

import com.vityarthi.voyageai.model.WeatherData;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service querying Open-Meteo Weather API with pinpoint accuracy for global cities and countries.
 */
public class WeatherService {

    private final HttpClient httpClient;
    private final Map<String, double[]> cityCoordMap;

    public WeatherService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        cityCoordMap = new HashMap<>();
        cityCoordMap.put("ICELAND", new double[]{64.1466, -21.9426});
        cityCoordMap.put("REYKJAVIK", new double[]{64.1466, -21.9426});
        cityCoordMap.put("PARIS", new double[]{48.8566, 2.3522});
        cityCoordMap.put("TOKYO", new double[]{35.6762, 139.6503});
        cityCoordMap.put("LONDON", new double[]{51.5074, -0.1278});
        cityCoordMap.put("NEW YORK", new double[]{40.7128, -74.0060});
        cityCoordMap.put("ROME", new double[]{41.9028, 12.4964});
        cityCoordMap.put("BALI", new double[]{-8.4095, 115.1889});
        cityCoordMap.put("DUBAI", new double[]{25.2048, 55.2708});
        cityCoordMap.put("SWITZERLAND", new double[]{46.8182, 8.2275});
        cityCoordMap.put("ZURICH", new double[]{47.3769, 8.5417});
        cityCoordMap.put("NORWAY", new double[]{59.9139, 10.7522});
        cityCoordMap.put("OSLO", new double[]{59.9139, 10.7522});
    }

    public WeatherData getWeatherForCity(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return getFallbackWeather("Unknown City");
        }

        String cleanCity = cityName.trim().toUpperCase();
        
        if (cityCoordMap.containsKey(cleanCity)) {
            double[] coords = cityCoordMap.get(cleanCity);
            return fetchWeatherByCoordinates(cityName.trim(), "Global Destination", coords[0], coords[1]);
        }

        try {
            String encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
            String geocodingUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + encodedCity + "&count=1&language=en&format=json";

            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(geocodingUrl))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> geoResponse = httpClient.send(geoRequest, HttpResponse.BodyHandlers.ofString());

            if (geoResponse.statusCode() == 200) {
                String body = geoResponse.body();
                double lat = parseDoubleFromJson(body, "\"latitude\":\\s*([0-9.-]+)");
                double lon = parseDoubleFromJson(body, "\"longitude\":\\s*([0-9.-]+)");
                String country = parseStringFromJson(body, "\"country\":\\s*\"([^\"]+)\"");

                if (lat != 0.0 || lon != 0.0) {
                    return fetchWeatherByCoordinates(cityName.trim(), country.isEmpty() ? "Destination" : country, lat, lon);
                }
            }
        } catch (Exception e) {
            // Fallback
        }

        return getFallbackWeather(cityName.trim());
    }

    private WeatherData fetchWeatherByCoordinates(String city, String country, double lat, double lon) {
        try {
            String weatherUrl = String.format("https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true", lat, lon);

            HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> weatherResponse = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

            if (weatherResponse.statusCode() == 200) {
                String json = weatherResponse.body();
                double temp = parseDoubleFromJson(json, "\"temperature\":\\s*([0-9.-]+)");
                double wind = parseDoubleFromJson(json, "\"windspeed\":\\s*([0-9.-]+)");
                int code = (int) parseDoubleFromJson(json, "\"weathercode\":\\s*([0-9]+)");

                String summary = mapWeatherCodeToText(code);
                return new WeatherData(city, country, lat, lon, temp, wind, code, summary);
            }
        } catch (Exception e) {
            // Fallback
        }
        return getFallbackWeather(city);
    }

    private String mapWeatherCodeToText(int code) {
        switch (code) {
            case 0: return "Clear Sky ☀️";
            case 1: case 2: case 3: return "Mainly Clear / Partly Cloudy ⛅";
            case 45: case 48: return "Foggy 🌫️";
            case 51: case 53: case 55: return "Light Drizzle 🌧️";
            case 61: case 63: case 65: return "Rainy 🌧️";
            case 71: case 73: case 75: return "Snowy ❄️";
            case 80: case 81: case 82: return "Rain Showers 🌦️";
            case 95: case 96: case 99: return "Thunderstorm 🌩️";
            default: return "Pleasant Weather 🌤️";
        }
    }

    public WeatherData getFallbackWeather(String cityName) {
        if (cityName.equalsIgnoreCase("ICELAND") || cityName.equalsIgnoreCase("REYKJAVIK")) {
            return new WeatherData(cityName, "Iceland", 64.14, -21.94, 5.5, 24.0, 3, "Chilly & Windy 🌧️❄️");
        }
        return new WeatherData(cityName, "Global Destination", 25.0, 55.0, 22.5, 11.0, 1, "Pleasant & Clear 🌤️");
    }

    private double parseDoubleFromJson(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private String parseStringFromJson(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
