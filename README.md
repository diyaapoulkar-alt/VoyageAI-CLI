# VoyageAI Travel Planner — Smart AI Travel Itineraries, Live Weather & Budget Platform

An elite travel intelligence platform and itinerary recommendation engine built with Java 17, Open-Meteo REST API, Google Gemini AI API, CSV file storage, and pure Java JDK HttpServer.

Featuring a glassmorphism web dashboard, real-time global weather forecasting, multi-category budget tracking, day-by-day AI travel itineraries, and weather-aware packing checklist analytics.

---

## Developer Profile

- **Developer**: Student Developer • Problem Solver
- **Institution**: VIT University — B.Tech in Computer Science Engineering
- **Project**: Programming in Java — Evaluated Project (VITyarthi Flipped Course)

---

## Key Features

- **Glassmorphism Web Dashboard**: High-contrast, responsive UI powered by standard Java JDK HttpServer on port 8080 with zero external framework overhead.
- **Open-Meteo Weather Engine**: Replaces static estimates with real-time temperature (Celsius and Fahrenheit), wind speed (km/h), and weather condition summaries.
- **AI Day-by-Day Itinerary Assistant**: Generates customized travel itineraries based on trip duration, budget, and live weather conditions, with automated fallback to an offline landmark database (Iceland, Italy, Paris, Tokyo, New York, Bali, London, Dubai, Switzerland, and more).
- **Weather-Smart Packing Recommender**: Intelligent clothing and equipment generator adjusting recommendations dynamically based on weather conditions (thermal gear and waterproof coats for sub-18 C or chilly climates vs. lightweight breathable apparel for warm climates).
- **Multi-Category Budget Ledger**: Create trip records, log expenses across six categories (Flight, Hotel, Food, Activity, Transport, Misc), calculate budget utilization percentages, and visualize spending progress.
- **Local CSV Data Persistence**: Store and manage trip and expense records in lightweight, human-readable CSV files (trips.csv and expenses.csv).
- **Automated System Verification**: Built-in 13-test unit testing suite verifying model getters, CSV serialization, input validation, and budget math.

---

## Architecture Overview

VoyageAI implements a formal 3-Tier Layered Architecture:

1. **Presentation Layer**:
   - `Main.java`: Terminal command-line interface (CLI).
   - `WebServer.java`: Pure Java HTTP Web Server on port 8080.
2. **Business Logic Layer**:
   - `TripService.java`: Manages budget calculations and category expense tracking.
   - `WeatherService.java`: Queries Open-Meteo REST API for live weather forecasts.
   - `GeminiAiService.java`: Connects to Google Gemini AI API with an offline fallback engine.
   - `InputValidator.java`: Validates dates, amounts, categories, and positive integers.
3. **Data & Persistence Layer**:
   - `FileStorageUtil.java`: Handles thread-safe reading and writing of CSV storage files.

---

## Tech Stack & Dependencies

| Layer | Technologies Used |
| :--- | :--- |
| **Language & Core** | Pure Java JDK 17+ |
| **UI Framework** | HTML5, CSS3 Glassmorphism System, Pure Java HttpServer |
| **Weather & Geocoding** | Open-Meteo REST API |
| **AI Engine** | Google Gemini AI REST API (`gemini-1.5-flash` / `gemini-2.0-flash`) |
| **Data & Persistence** | Human-readable CSV file storage (`trips.csv`, `expenses.csv`) |
| **Testing** | Standard Java Unit Test Suite (`VoyageAiTest.java`) |

---

## Local Installation & Setup

### 1. Prerequisites
Ensure you have Java Development Kit (JDK 17 or higher) installed on your system.

### 2. Clone the Repository
```bash
git clone https://github.com/diyaapoulkar-alt/VoyageAI-CLI.git
cd "Voyage AI Travel Planner"
```

### 3. Compile the Java Source Code
Open a terminal in the root project directory and run:

**On Windows (PowerShell):**
```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
```

**On Linux / macOS / Git Bash:**
```bash
find src -name "*.java" | xargs javac -d bin
```

---

## Running the Application

### Launch the Web Interface (Port 8080)
Run the following command to start the web application server:

```bash
java -cp bin com.vityarthi.voyageai.WebMain
```

Open your browser and navigate to: **http://localhost:8080**

---

### Launch the Command-Line Terminal (CLI) Interface
Run the following command to start the interactive terminal application:

```bash
java -cp bin com.vityarthi.voyageai.Main
```

---

### Run Automated Unit Tests
Run the following command to execute the test suite:

```bash
java -cp bin com.vityarthi.voyageai.VoyageAiTest
```

---

## Project Structure

```text
Voyage AI Travel Planner/
├── src/
│   └── com/vityarthi/voyageai/
│       ├── Main.java                # Command-Line CLI Entrypoint
│       ├── WebMain.java             # Web Application Server Entrypoint
│       ├── VoyageAiTest.java        # Automated Unit Test Suite (13 Tests)
│       ├── model/
│       │   ├── Trip.java            # Trip Data Model
│       │   ├── Expense.java         # Expense Data Model
│       │   ├── WeatherData.java     # Weather Data Model
│       │   └── ItineraryPlace.java  # Itinerary Location Model
│       ├── service/
│       │   ├── TripService.java     # Budget Calculation & Expense Logic
│       │   ├── WeatherService.java  # Open-Meteo REST API Service
│       │   └── GeminiAiService.java # Gemini AI & Offline Rule Engine
│       ├── util/
│       │   ├── FileStorageUtil.java # CSV File Read/Write Utilities
│       │   └── InputValidator.java  # User Input Validation & Formatting
│       └── web/
│           └── WebServer.java       # Pure Java JDK HttpServer Handler
├── bin/                             # Compiled Java Bytecode (.class)
├── data/
│   ├── trips.csv                    # CSV File Storage for Trips
│   └── expenses.csv                 # CSV File Storage for Expenses
├── statement.md                     # Problem & Scope Statement
└── README.md                        # Comprehensive Repository Documentation
```

---

## Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

---

## License

Distributed under the MIT License. See LICENSE for details.
