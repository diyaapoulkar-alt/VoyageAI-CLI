package com.vityarthi.voyageai.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.vityarthi.voyageai.model.Trip;
import com.vityarthi.voyageai.model.WeatherData;
import com.vityarthi.voyageai.service.GeminiAiService;
import com.vityarthi.voyageai.service.TripService;
import com.vityarthi.voyageai.service.WeatherService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Built-in Pure Java Web Server powering the VoyageAI Web Interface.
 * Uses com.sun.net.httpserver.HttpServer (Standard JDK 11+).
 */
public class WebServer {

    private final int port;
    private final WeatherService weatherService;
    private final GeminiAiService geminiAiService;
    private final TripService tripService;
    private HttpServer server;

    public WebServer(int port) {
        this.port = port;
        this.weatherService = new WeatherService();
        this.geminiAiService = new GeminiAiService();
        this.tripService = new TripService();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticUiHandler());
        server.createContext("/api/weather", new WeatherApiHandler());
        server.createContext("/api/itinerary", new ItineraryApiHandler());
        server.createContext("/api/trips", new TripsApiHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("==========================================================================");
        System.out.println(" 🚀 VoyageAI Web Server Started Successfully!");
        System.out.println(" 🌐 Open your web browser and visit: http://localhost:" + port);
        System.out.println("==========================================================================");
    }

    private class StaticUiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = getWebUiHtml();
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private class WeatherApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String city = "Paris";
            if (query != null && query.contains("city=")) {
                city = query.split("city=")[1].split("&")[0];
                city = java.net.URLDecoder.decode(city, StandardCharsets.UTF_8);
            }
            WeatherData wd = weatherService.getWeatherForCity(city);
            String json = String.format(
                "{\"city\":\"%s\",\"country\":\"%s\",\"temperatureC\":%.1f,\"temperatureF\":%.1f,\"windSpeedKmh\":%.1f,\"conditionSummary\":\"%s\"}",
                escapeJson(wd.getCity()), escapeJson(wd.getCountry()), wd.getTemperatureC(), wd.getTemperatureF(), wd.getWindSpeedKmh(), escapeJson(wd.getConditionSummary())
            );
            sendJsonResponse(exchange, json);
        }
    }

    private class ItineraryApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String city = extractJsonField(body, "destination");
                if (city.isEmpty()) city = "Tokyo";
                
                int duration = 5;
                try { duration = Integer.parseInt(extractJsonField(body, "duration")); } catch (Exception e) {}
                
                double budget = 1000;
                try { budget = Double.parseDouble(extractJsonField(body, "budget")); } catch (Exception e) {}

                String apiKey = extractJsonField(body, "apiKey");

                WeatherData weather = weatherService.getWeatherForCity(city);
                String weatherSummary = String.format("%.1f°C, %s", weather.getTemperatureC(), weather.getConditionSummary());
                String itineraryText = geminiAiService.generateItinerary(city, duration, budget, weatherSummary, apiKey);
                String packingText = geminiAiService.generatePackingList(city, duration, weatherSummary, apiKey);

                String json = String.format(
                    "{\"destination\":\"%s\",\"duration\":%d,\"budget\":%.2f,\"weather\":\"%s\",\"tempC\":%.1f,\"itineraryText\":\"%s\",\"packingText\":\"%s\"}",
                    escapeJson(city), duration, budget, escapeJson(weather.getConditionSummary()), weather.getTemperatureC(),
                    escapeJson(itineraryText), escapeJson(packingText)
                );
                sendJsonResponse(exchange, json);
            } else {
                exchange.sendResponseHeaders(455, -1);
            }
        }
    }

    private class TripsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Trip> trips = tripService.getAllTrips();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < trips.size(); i++) {
                Trip t = trips.get(i);
                double spent = tripService.getTotalSpentForTrip(t.getId());
                sb.append(String.format(
                    "{\"id\":\"%s\",\"destination\":\"%s\",\"country\":\"%s\",\"duration\":%d,\"budget\":%.2f,\"spent\":%.2f}",
                    t.getId(), escapeJson(t.getDestination()), escapeJson(t.getCountry()), t.getDurationDays(), t.getBudgetUsd(), spent
                ));
                if (i < trips.size() - 1) sb.append(",");
            }
            sb.append("]");
            sendJsonResponse(exchange, sb.toString());
        }
    }

    private void sendJsonResponse(HttpExchange exchange, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String escapeJson(String text) {
        return text == null ? "" : text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private static String extractJsonField(String json, String key) {
        if (json == null || json.isEmpty()) return "";
        String cleanJson = json.replace("\\\"", "\"");
        int idx = cleanJson.indexOf("\"" + key + "\"");
        if (idx == -1) {
            idx = cleanJson.toLowerCase().indexOf("\"" + key.toLowerCase() + "\"");
            if (idx == -1) return "";
        }
        int colon = cleanJson.indexOf(":", idx);
        if (colon == -1) return "";

        int startQuote = cleanJson.indexOf("\"", colon);
        if (startQuote != -1) {
            int endQuote = cleanJson.indexOf("\"", startQuote + 1);
            if (endQuote != -1) {
                return cleanJson.substring(startQuote + 1, endQuote).trim();
            }
        }

        int numStart = colon + 1;
        while (numStart < cleanJson.length() && (cleanJson.charAt(numStart) == ' ' || cleanJson.charAt(numStart) == ':')) numStart++;
        int numEnd = numStart;
        while (numEnd < cleanJson.length() && (Character.isDigit(cleanJson.charAt(numEnd)) || cleanJson.charAt(numEnd) == '.' || cleanJson.charAt(numEnd) == '-')) numEnd++;
        return cleanJson.substring(numStart, numEnd).trim();
    }

    private String getWebUiHtml() {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>VoyageAI — Smart Travel Planner & Itinerary Assistant</title>
            <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
            <style>
                :root {
                    --primary: #2563eb;
                    --primary-dark: #1d4ed8;
                    --accent: #0d9488;
                    --bg-gradient: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #0f172a 100%);
                    --card-bg: rgba(30, 41, 59, 0.7);
                    --card-border: rgba(255, 255, 255, 0.1);
                    --text-light: #f8fafc;
                    --text-muted: #94a3b8;
                }
                * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', sans-serif; }
                body { background: var(--bg-gradient); color: var(--text-light); min-height: 100vh; padding: 20px; }
                .container { max-width: 1200px; margin: 0 auto; }
                header { text-align: center; padding: 40px 0 20px; }
                header h1 { font-size: 2.8rem; font-weight: 800; background: linear-gradient(90deg, #60a5fa, #2dd4bf); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 10px; }
                header p { color: var(--text-muted); font-size: 1.1rem; }
                
                .glass-card { background: var(--card-bg); backdrop-filter: blur(16px); border: 1px solid var(--card-border); border-radius: 20px; padding: 30px; margin-bottom: 30px; box-shadow: 0 20px 40px rgba(0,0,0,0.4); }
                
                .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; align-items: end; }
                .form-group label { display: block; font-size: 0.9rem; font-weight: 600; margin-bottom: 8px; color: #cbd5e1; }
                .form-group input, .form-group select { width: 100%; padding: 14px 18px; border-radius: 12px; border: 1px solid var(--card-border); background: rgba(15, 23, 42, 0.8); color: #fff; font-size: 1rem; outline: none; transition: 0.3s; }
                .form-group input:focus { border-color: #60a5fa; box-shadow: 0 0 0 4px rgba(96, 165, 250, 0.2); }
                
                .btn-primary { width: 100%; padding: 16px; border: none; border-radius: 12px; background: linear-gradient(90deg, #2563eb, #0d9488); color: #fff; font-weight: 700; font-size: 1.05rem; cursor: pointer; transition: 0.3s; box-shadow: 0 10px 20px rgba(37, 99, 235, 0.3); }
                .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 15px 30px rgba(37, 99, 235, 0.4); }
                
                .results-grid { display: grid; grid-template-columns: 1fr 2fr; gap: 30px; }
                @media (max-width: 900px) { .results-grid { grid-template-columns: 1fr; } }
                
                .weather-card { background: linear-gradient(135deg, rgba(37, 99, 235, 0.2), rgba(13, 148, 136, 0.2)); border: 1px solid rgba(45, 212, 191, 0.3); border-radius: 16px; padding: 24px; text-align: center; }
                .temp-display { font-size: 3.5rem; font-weight: 800; color: #38bdf8; margin: 10px 0; }
                
                .places-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 20px; margin-top: 20px; }
                .place-card { background: rgba(15, 23, 42, 0.6); border: 1px solid var(--card-border); border-radius: 16px; padding: 20px; transition: 0.3s; }
                .place-card:hover { border-color: #2dd4bf; transform: translateY(-4px); }
                .badge { display: inline-block; padding: 4px 10px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; text-transform: uppercase; background: rgba(45, 212, 191, 0.15); color: #2dd4bf; margin-bottom: 10px; }
                
                .itinerary-output { white-space: pre-wrap; font-size: 0.95rem; line-height: 1.7; color: #e2e8f0; background: rgba(15, 23, 42, 0.6); padding: 20px; border-radius: 14px; border: 1px solid var(--card-border); max-height: 500px; overflow-y: auto; }
                .loading-spinner { display: none; text-align: center; padding: 30px; }
            </style>
        </head>
        <body>
            <div class="container">
                <header>
                    <h1>✈️ VoyageAI Travel Planner</h1>
                    <p>AI-Powered Travel Itineraries, Live Weather Forecasts & Budget Recommendations</p>
                </header>

                <div class="glass-card">
                    <form id="plannerForm" onsubmit="generateItinerary(event)">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>Destination City</label>
                                <input type="text" id="destination" placeholder="e.g. Paris, Tokyo, Bali, New York" value="Paris" required>
                            </div>
                            <div class="form-group">
                                <label>Total Budget ($ USD)</label>
                                <input type="number" id="budget" placeholder="1500" value="1500" min="50" required>
                            </div>
                            <div class="form-group">
                                <label>Duration (Days)</label>
                                <input type="number" id="duration" placeholder="5" value="5" min="1" max="30" required>
                            </div>
                            <div class="form-group">
                                <label>Google Gemini API Key (Optional)</label>
                                <input type="password" id="apiKey" placeholder="Leave blank for offline AI engine">
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn-primary">Generate Smart Itinerary 🚀</button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="loading-spinner" id="spinner">
                    <h2>Fetching Live Weather & Generating AI Itinerary...</h2>
                </div>

                <div class="results-grid" id="resultsSection">
                    <div>
                        <div class="weather-card">
                            <h3 id="weatherCity">Paris, France</h3>
                            <div class="temp-display" id="weatherTemp">20.8°C</div>
                            <p id="weatherCond">Partly Cloudy ⛅</p>
                            <p style="margin-top: 10px; font-size: 0.85rem; color: #94a3b8;" id="weatherWind">Wind: 12.8 km/h</p>
                        </div>
                        
                        <div class="glass-card" style="margin-top: 20px;">
                            <h3>📦 Weather-Smart Packing List</h3>
                            <div id="packingOutput" class="itinerary-output" style="margin-top: 15px; font-size: 0.85rem;">
                                Loading packing checklist...
                            </div>
                        </div>
                    </div>

                    <div>
                        <div class="glass-card">
                            <h2>🗺️ Recommended Day-by-Day Itinerary</h2>
                            <div id="itineraryOutput" class="itinerary-output" style="margin-top: 15px;">
                                Enter your destination and budget above to generate your customized trip plan!
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                async function generateItinerary(event) {
                    event.preventDefault();
                    const dest = document.getElementById('destination').value;
                    const budget = document.getElementById('budget').value;
                    const duration = document.getElementById('duration').value;
                    const apiKey = document.getElementById('apiKey').value;

                    document.getElementById('spinner').style.display = 'block';
                    document.getElementById('resultsSection').style.opacity = '0.4';

                    try {
                        const res = await fetch('/api/itinerary', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ destination: dest, budget: budget, duration: duration, apiKey: apiKey })
                        });
                        const data = await res.json();

                        document.getElementById('weatherCity').innerText = data.destination;
                        document.getElementById('weatherTemp').innerText = data.tempC.toFixed(1) + '°C';
                        document.getElementById('weatherCond').innerText = data.weather;
                        document.getElementById('itineraryOutput').innerText = data.itineraryText;
                        document.getElementById('packingOutput').innerText = data.packingText;

                    } catch (err) {
                        alert('Error generating itinerary: ' + err);
                    } finally {
                        document.getElementById('spinner').style.display = 'none';
                        document.getElementById('resultsSection').style.opacity = '1';
                    }
                }

                // Initial load weather for Paris
                window.onload = function() {
                    fetch('/api/weather?city=Paris')
                        .then(r => r.json())
                        .then(d => {
                            document.getElementById('weatherCity').innerText = d.city + ', ' + d.country;
                            document.getElementById('weatherTemp').innerText = d.temperatureC.toFixed(1) + '°C';
                            document.getElementById('weatherCond').innerText = d.conditionSummary;
                            document.getElementById('weatherWind').innerText = 'Wind: ' + d.windSpeedKmh.toFixed(1) + ' km/h';
                        });
                };
            </script>
        </body>
        </html>
        """;
    }
}
