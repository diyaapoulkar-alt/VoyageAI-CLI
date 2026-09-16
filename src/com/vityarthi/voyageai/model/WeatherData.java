package com.vityarthi.voyageai.model;

/**
 * Data model for weather information retrieved from Open-Meteo API.
 */
public class WeatherData {
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private double temperatureC;
    private double windSpeedKmh;
    private int weatherCode;
    private String conditionSummary;

    public WeatherData() {}

    public WeatherData(String city, String country, double latitude, double longitude, double temperatureC, double windSpeedKmh, int weatherCode, String conditionSummary) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperatureC = temperatureC;
        this.windSpeedKmh = windSpeedKmh;
        this.weatherCode = weatherCode;
        this.conditionSummary = conditionSummary;
    }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getTemperatureC() { return temperatureC; }
    public double getTemperatureF() { return (temperatureC * 9 / 5) + 32; }
    public double getWindSpeedKmh() { return windSpeedKmh; }
    public int getWeatherCode() { return weatherCode; }
    public String getConditionSummary() { return conditionSummary; }

    @Override
    public String toString() {
        return String.format("City: %s, %s (Lat: %.2f, Lon: %.2f) | Temp: %.1f°C (%.1f°F) | Wind: %.1f km/h | Condition: %s",
            city, country, latitude, longitude, temperatureC, getTemperatureF(), windSpeedKmh, conditionSummary);
    }
}
