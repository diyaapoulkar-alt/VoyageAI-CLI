package com.vityarthi.voyageai.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GeminiAiService {

    private final HttpClient httpClient;
    private final Map<String, String[]> famousAttractionsDatabase;

    public GeminiAiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();

        // Database of famous landmarks per city/country
        famousAttractionsDatabase = new HashMap<>();
        famousAttractionsDatabase.put("ICELAND", new String[]{
            "♨️ Blue Lagoon Geothermal Spa", "🌋 Golden Circle (Thingvellir, Geysir, Gullfoss)",
            "🌌 Northern Lights (Aurora Borealis) Tour", "🧊 Jökulsárlón Glacier Lagoon & Diamond Beach",
            "🌊 Reynisfjara Black Sand Beach"
        });
        famousAttractionsDatabase.put("REYKJAVIK", new String[]{
            "♨️ Blue Lagoon Geothermal Spa", "🌋 Golden Circle (Thingvellir, Geysir, Gullfoss)",
            "🌌 Northern Lights (Aurora Borealis) Tour", "🧊 Jökulsárlón Glacier Lagoon & Diamond Beach",
            "🌊 Reynisfjara Black Sand Beach"
        });
        famousAttractionsDatabase.put("ITALY", new String[]{
            "🏛️ Colosseum & Roman Forum (Rome)", "⛲ Trevi Fountain & Pantheon (Rome)",
            "🚣 Grand Canal & St. Mark's Square (Venice)", "🎨 Florence Duomo & Uffizi Gallery",
            "🗼 Leaning Tower of Pisa", "🌊 Amalfi Coast & Positano"
        });
        famousAttractionsDatabase.put("ROME", new String[]{
            "🏛️ Colosseum & Roman Forum", "⛲ Trevi Fountain", "🏛️ Pantheon",
            "⛪ St. Peter's Basilica & Vatican Museums", "🏛️ Spanish Steps"
        });
        famousAttractionsDatabase.put("VENICE", new String[]{
            "🚣 Grand Canal Gondola Ride", "⛪ St. Mark's Basilica & Piazza San Marco",
            "🌉 Rialto Bridge", "🏰 Doge's Palace", "🏝️ Murano & Burano Glass Islands"
        });
        famousAttractionsDatabase.put("FLORENCE", new String[]{
            "🎨 Uffizi Gallery", "⛪ Cathedral of Santa Maria del Fiore (Duomo)",
            "🌉 Ponte Vecchio Bridge", "🖼️ Galleria dell'Accademia (David Statue)", "🌅 Piazzale Michelangelo"
        });
        famousAttractionsDatabase.put("FRANCE", new String[]{
            "🗼 Eiffel Tower & Champ de Mars (Paris)", "🏛️ Louvre Museum (Paris)",
            "🏰 Palace of Versailles", "🏖️ French Riviera & Nice Promenade", "🏰 Mont Saint-Michel"
        });
        famousAttractionsDatabase.put("PARIS", new String[]{
            "🏛️ Louvre Museum & Glass Pyramid", "🗼 Eiffel Tower & Champ de Mars",
            "⛪ Sacré-Cœur Basilica & Montmartre", "⛵ Seine River Sightseeing Cruise",
            "🏛️ Arc de Triomphe & Champs-Élysées", "🎨 Musée d'Orsay", "🏰 Palace of Versailles"
        });
        famousAttractionsDatabase.put("JAPAN", new String[]{
            "🏔️ Mount Fuji & Lake Kawaguchiko", "⛩️ Senso-ji Temple (Tokyo)",
            "⛩️ Fushimi Inari Shrine & Bamboo Grove (Kyoto)", "🍢 Dotonbori Street Food (Osaka)", "🕊️ Hiroshima Peace Memorial Park"
        });
        famousAttractionsDatabase.put("TOKYO", new String[]{
            "⛩️ Senso-ji Temple & Nakamise Street", "🏙️ Shibuya Crossing & Hachiko Statue",
            "🗼 Tokyo Tower & Red Tower Deck", "🌳 Meiji Shrine & Yoyogi Park",
            "🐟 Tsukiji Outer Market", "⚡ Akihabara Electric Town", "🌸 Ueno Park"
        });
        famousAttractionsDatabase.put("KYOTO", new String[]{
            "⛩️ Fushimi Inari Taisha (10,000 Torii Gates)", "🎋 Arashiyama Bamboo Grove",
            "✨ Kinkaku-ji (Golden Pavilion)", "🍵 Gion Geisha District", "⛩️ Kiyomizu-dera Temple"
        });
        famousAttractionsDatabase.put("SPAIN", new String[]{
            "⛪ Sagrada Família (Barcelona)", "🌳 Park Güell (Barcelona)",
            "👑 Royal Palace of Madrid", "🏰 Alhambra Palace (Granada)", "🏛️ Plaza de España (Seville)"
        });
        famousAttractionsDatabase.put("BARCELONA", new String[]{
            "⛪ Basílica de la Sagrada Família", "🌳 Park Güell",
            "🏠 Casa Batlló & La Pedrera", "🛍️ La Rambla Boulevard", "🏰 Gothic Quarter (Barri Gòtic)"
        });
        famousAttractionsDatabase.put("GERMANY", new String[]{
            "🏛️ Brandenburg Gate (Berlin)", "🏰 Neuschwanstein Castle (Bavaria)",
            "⛪ Cologne Cathedral", "🏙️ Marienplatz (Munich)", "🌲 Black Forest & Lake Titisee"
        });
        famousAttractionsDatabase.put("BERLIN", new String[]{
            "🏛️ Brandenburg Gate", "🏛️ Reichstag Building & Glass Dome",
            "🎨 Berlin Wall Memorial & East Side Gallery", "🏛️ Museum Island", "🎖️ Checkpoint Charlie"
        });
        famousAttractionsDatabase.put("INDIA", new String[]{
            "🕌 Taj Mahal (Agra)", "🏛️ Red Fort & Qutub Minar (Delhi)",
            "🏛️ Gateway of India (Mumbai)", "🏰 Amber Palace (Jaipur)", "🌴 Kerala Backwaters Cruise"
        });
        famousAttractionsDatabase.put("DELHI", new String[]{
            "🏛️ Red Fort & Chandni Chowk Market", "🏛️ Qutub Minar Complex",
            "🏛️ India Gate & Kartavya Path", "🪷 Lotus Temple", "🏛️ Humayun's Tomb"
        });
        famousAttractionsDatabase.put("MUMBAI", new String[]{
            "🏛️ Gateway of India", "🌊 Marine Drive (Queen's Necklace)",
            "🚂 Chhatrapati Shivaji Maharaj Terminus", "🏝️ Elephanta Caves", "🛍️ Colaba Causeway Market"
        });
        famousAttractionsDatabase.put("USA", new String[]{
            "🗽 Statue of Liberty (New York)", "🏞️ Grand Canyon National Park",
            "🌉 Golden Gate Bridge (San Francisco)", "🎢 Walt Disney World (Orlando)", "🎭 Times Square (New York)"
        });
        famousAttractionsDatabase.put("NEW YORK", new String[]{
            "🗽 Statue of Liberty & Ellis Island", "🏙️ Empire State Building",
            "🌳 Central Park", "🎭 Times Square & Broadway", "🌉 Brooklyn Bridge"
        });
        famousAttractionsDatabase.put("UK", new String[]{
            "🕰️ Big Ben & London Eye (London)", "🏰 Tower of London",
            "🪨 Stonehenge Prehistoric Monument", "🏰 Edinburgh Castle (Scotland)", "🏛️ Roman Baths (Bath)"
        });
        famousAttractionsDatabase.put("LONDON", new String[]{
            "🕰️ Big Ben & Palace of Westminster", "🎡 London Eye",
            "🏰 Tower of London & Tower Bridge", "🏛️ British Museum", "👑 Buckingham Palace"
        });
        famousAttractionsDatabase.put("SWITZERLAND", new String[]{
            "🏔️ Matterhorn & Zermatt", "🏔️ Jungfraujoch Top of Europe",
            "🌊 Lake Geneva & Chillon Castle", "🚂 Glacier Express Train", "🏞️ Interlaken & Lauterbrunnen Valley"
        });
        famousAttractionsDatabase.put("ZURICH", new String[]{
            "🏞️ Lake Zurich Promenade", "⛪ Grossmünster Church",
            "🛍️ Bahnhofstrasse Shopping", "🏰 Swiss National Museum", "🌄 Uetliberg Mountain Viewpoint"
        });
        famousAttractionsDatabase.put("DUBAI", new String[]{
            "🏙️ Burj Khalifa Observation Deck", "🛍️ Dubai Mall & Fountain Show",
            "🏜️ Desert Safari & Dune Bashing", "🌴 Palm Jumeirah & Atlantis", "🕌 Grand Mosque & Gold Souk"
        });
        famousAttractionsDatabase.put("BALI", new String[]{
            "🌴 Tegallalang Rice Terraces", "🐒 Ubud Monkey Forest",
            "🌅 Tanah Lot Sea Temple", "🌊 Uluwatu Cliffside Temple", "🌋 Mount Batur Sunrise Trek"
        });
        famousAttractionsDatabase.put("EGYPT", new String[]{
            "📐 Great Pyramids of Giza & Sphinx", "🏛️ Karnak & Luxor Temples",
            "🚢 Nile River Cruise", "🏺 Egyptian Museum (Cairo)", "🏜️ Abu Simbel Temples"
        });
        famousAttractionsDatabase.put("AUSTRALIA", new String[]{
            "🎭 Sydney Opera House & Harbour Bridge", "🪸 Great Barrier Reef",
            "🪨 Uluru (Ayers Rock)", "🌊 Bondi Beach", "🚗 Great Ocean Road"
        });
        famousAttractionsDatabase.put("THAILAND", new String[]{
            "🛕 Grand Palace & Wat Phra Kaew (Bangkok)", "🏝️ Phi Phi Islands",
            "🐘 Elephant Nature Park (Chiang Mai)", "🛕 Wat Arun Temple", "🌃 Chiang Mai Night Bazaar"
        });
        famousAttractionsDatabase.put("GREECE", new String[]{
            "🏛️ Acropolis & Parthenon (Athens)", "🌅 Oia Sunset Viewpoint (Santorini)",
            "🏖️ Mykonos Old Town & Windmills", "🏛️ Ancient Delphi", "🏝️ Navagio Shipwreck Beach"
        });
        famousAttractionsDatabase.put("TURKEY", new String[]{
            "🕌 Hagia Sophia & Blue Mosque (Istanbul)", "🎈 Cappadocia Hot Air Balloon Ride",
            "🏛️ Ephesus Ancient City", "💦 Pamukkale Thermal Terraces", "🛍️ Grand Bazaar (Istanbul)"
        });
    }

    public String generateItinerary(String destination, int durationDays, double budgetUsd, String weatherSummary, String apiKey) {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            String promptText = String.format(
                "You are an expert AI Travel Planner. Create a detailed %d-day travel itinerary for %s with a total budget of $%.2f USD.\n" +
                "Current Weather Forecast: %s.\n" +
                "STRICT REQUIREMENTS:\n" +
                "1. Provide a section titled '🌟 TOP FAMOUS PLACES TO VISIT IN %s' explicitly naming famous real landmarks & attractions for this location.\n" +
                "2. Provide a Day-by-Day schedule incorporating these specific famous places.\n" +
                "3. Include famous local food dishes to try and budget allocation advice.",
                durationDays, destination, budgetUsd, weatherSummary, destination.toUpperCase()
            );

            String aiResponse = queryGeminiApi(promptText, apiKey);
            if (aiResponse != null && !aiResponse.trim().isEmpty()) {
                return aiResponse;
            }
        }

        return generateOfflineItinerary(destination, durationDays, budgetUsd, weatherSummary);
    }

    public String generatePackingList(String destination, int durationDays, String weatherSummary, String apiKey) {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            String promptText = String.format(
                "Create a weather-smart packing checklist for a %d-day trip to %s. Current weather forecast: %s.\n" +
                "STRICT REQUIREMENTS:\n" +
                "Categorize into Clothes, Toiletries, Electronics, and Travel Documents.\n" +
                "Tailor clothes specifically to current weather and temperature: recommend heavy cold gear (thermals, parka, beanie, boots) if chilly/cold/rainy/snowy/sub-18°C, or light breathable summer clothes (linen, shorts, sunglasses) if warm/sunny/tropical.",
                durationDays, destination, weatherSummary
            );

            String aiResponse = queryGeminiApi(promptText, apiKey);
            if (aiResponse != null && !aiResponse.trim().isEmpty()) {
                return aiResponse;
            }
        }

        return generateOfflinePackingList(destination, weatherSummary);
    }

    private String queryGeminiApi(String promptText, String apiKey) {
        String[] models = new String[]{"gemini-1.5-flash", "gemini-2.0-flash", "gemini-2.5-flash"};
        for (String model : models) {
            try {
                String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey.trim();
                String jsonPayload = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", 
                        promptText.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", ""));

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(12))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return extractTextFromGeminiResponse(response.body());
                }
            } catch (Exception ignored) {
                // Try next model endpoint
            }
        }
        return null;
    }

    private String extractTextFromGeminiResponse(String json) {
        try {
            int textKey = json.indexOf("\"text\":");
            if (textKey == -1) textKey = json.indexOf("\"text\" :");
            if (textKey != -1) {
                int startQuote = json.indexOf("\"", textKey + 6);
                if (startQuote != -1) {
                    StringBuilder sb = new StringBuilder();
                    boolean escaped = false;
                    for (int i = startQuote + 1; i < json.length(); i++) {
                        char c = json.charAt(i);
                        if (escaped) {
                            if (c == 'n') sb.append('\n');
                            else if (c == 't') sb.append('\t');
                            else if (c == 'r') sb.append('\r');
                            else sb.append(c);
                            escaped = false;
                        } else if (c == '\\') {
                            escaped = true;
                        } else if (c == '"') {
                            break;
                        } else {
                            sb.append(c);
                        }
                    }
                    return sb.toString().trim();
                }
            }
        } catch (Exception ignored) {}
        return json;
    }

    private String[] getFamousPlacesForDestination(String destination) {
        String clean = destination.trim().toUpperCase();
        if (famousAttractionsDatabase.containsKey(clean)) {
            return famousAttractionsDatabase.get(clean);
        }
        for (Map.Entry<String, String[]> entry : famousAttractionsDatabase.entrySet()) {
            if (clean.contains(entry.getKey()) || entry.getKey().contains(clean)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String generateOfflineItinerary(String destination, int durationDays, double budgetUsd, String weatherSummary) {
        StringBuilder sb = new StringBuilder();
        String[] places = getFamousPlacesForDestination(destination);

        sb.append("🌟 TOP FAMOUS PLACES TO VISIT IN ").append(destination.toUpperCase()).append("\n");
        sb.append("=====================================================\n");
        if (places != null && places.length > 0) {
            for (String p : places) {
                sb.append("  • ").append(p).append("\n");
            }
        } else {
            places = new String[]{
                "Historic " + destination + " City Center",
                "National Cultural Museum of " + destination,
                "Central Promenade & Riverfront",
                "Artisan Craft Market",
                "Panoramic Sunset Viewpoint"
            };
            sb.append("  • 🏛️ Historic ").append(destination).append(" City Center & Main Landmark Square\n");
            sb.append("  • 🎨 Central National Museum & Art Gallery\n");
            sb.append("  • ⛵ Scenic Waterfront Promenade & River Cruise\n");
            sb.append("  • 🛍️ Artisan Craft Market & Local Bazaar\n");
            sb.append("  • 🌅 Sunset Scenic Viewpoint Deck\n");
        }

        sb.append("\n🗓️ ").append(durationDays).append("-DAY WEATHER-AWARE ITINERARY (Budget: $").append(String.format("%.2f", budgetUsd)).append(")\n");
        sb.append("Current Weather: ").append(weatherSummary).append("\n");
        sb.append("=====================================================\n");

        for (int i = 1; i <= Math.min(durationDays, 5); i++) {
            sb.append("📍 Day ").append(i).append(":\n");
            String mainSpot = places[(i - 1) % places.length];
            if (i == 1) {
                sb.append("   • Morning: Arrival, hotel check-in, and neighborhood walk.\n");
                sb.append("   • Afternoon: Visit ").append(mainSpot).append(".\n");
                sb.append("   • Evening: Welcome dinner & local street food tasting.\n");
            } else if (i == 2) {
                sb.append("   • Morning: Guided tour of ").append(mainSpot).append(".\n");
                sb.append("   • Afternoon: Explore regional cultural exhibits & gardens.\n");
                sb.append("   • Evening: Sunset photo session at city viewpoint.\n");
            } else if (i == 3) {
                sb.append("   • Morning: Visit ").append(mainSpot).append(".\n");
                sb.append("   • Afternoon: Souvenir shopping & local artisan markets.\n");
                sb.append("   • Evening: Traditional dinner & live evening music.\n");
            } else {
                sb.append("   • Morning: Visit ").append(mainSpot).append(".\n");
                sb.append("   • Afternoon: Relax at scenic park & cafe walk.\n");
                sb.append("   • Evening: Farewell dinner and packing.\n");
            }
        }

        sb.append("\n🍲 Famous Local Foods to Try: Regional specialties, artisan bakery items, and fresh local street snacks.\n");
        sb.append("💡 Budget Advice: Allocate 40% for accommodations, 30% for food, 20% for entry tickets, and 10% for local transport.");
        return sb.toString();
    }

    public String generateOfflinePackingList(String destination, String weatherSummary) {
        StringBuilder sb = new StringBuilder();
        sb.append("📦 [VoyageAI Weather-Smart Packing Checklist]\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Target Destination: ").append(destination).append(" (Weather: ").append(weatherSummary).append(")\n");
        sb.append("--------------------------------------------------\n");

        double temp = extractTemp(weatherSummary);
        String destUpper = destination.toUpperCase();
        String weatherLower = weatherSummary.toLowerCase();

        boolean isCold = destUpper.contains("ICELAND") 
                      || destUpper.contains("REYKJAVIK")
                      || destUpper.contains("NORWAY")
                      || destUpper.contains("SWITZERLAND")
                      || destUpper.contains("GREENLAND")
                      || destUpper.contains("ALASKA")
                      || destUpper.contains("FINLAND")
                      || destUpper.contains("SWEDEN")
                      || destUpper.contains("RUSSIA")
                      || destUpper.contains("CANADA")
                      || destUpper.contains("DENMARK")
                      || weatherLower.contains("chilly")
                      || weatherLower.contains("snow")
                      || weatherLower.contains("cold")
                      || weatherLower.contains("fog")
                      || weatherLower.contains("drizzle")
                      || weatherLower.contains("rain")
                      || temp < 18.0;

        if (isCold) {
            sb.append("👕 Clothes: Thermal base layers, fleece sweater, wind & waterproof parka jacket, beanie, insulated gloves, waterproof hiking boots.\n");
        } else if (temp >= 28.0 || weatherLower.contains("sunny") || weatherLower.contains("clear")) {
            sb.append("👕 Clothes: Lightweight breathable linen tops, shorts/skirt, UV sunglasses, sun hat, comfortable sandals/sneakers.\n");
        } else {
            sb.append("👕 Clothes: Light cardigan/jacket, cotton t-shirts, comfortable trousers/jeans, walking shoes, compact umbrella.\n");
        }
        sb.append("🧼 Toiletries: High-SPF sunscreen, lip balm, moisturizing lotion, travel toothbrush, hand sanitizer.\n");
        sb.append("⚡ Electronics: Universal power adapter, high-capacity power bank, phone charger, camera with extra batteries.\n");
        sb.append("🛂 Documents: Passport/ID, flight tickets, travel insurance copy, emergency cash.\n");
        return sb.toString();
    }

    private double extractTemp(String weatherSummary) {
        try {
            int idx = weatherSummary.indexOf("°C");
            if (idx != -1) {
                int start = idx - 1;
                while (start >= 0 && (Character.isDigit(weatherSummary.charAt(start)) || weatherSummary.charAt(start) == '.' || weatherSummary.charAt(start) == '-')) {
                    start--;
                }
                return Double.parseDouble(weatherSummary.substring(start + 1, idx).trim());
            }
        } catch (Exception ignored) {}
        return 20.0;
    }
}
