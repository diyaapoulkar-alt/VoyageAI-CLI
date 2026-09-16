# VoyageAI Travel Planner — Smart AI Travel Itineraries, Live Weather & Budget Platform

---

## Developer Profile

- **Developer**: Student Developer • Problem Solver
- **Institution**: VIT University — B.Tech in Computer Science Engineering
- **Project**: Programming in Java — Evaluated Project (VITyarthi Flipped Course)

---

## Overview of the Project

VoyageAI Travel Planner is an elite travel intelligence platform and itinerary recommendation engine built with Java 17, Open-Meteo REST API, Google Gemini AI API, CSV file storage, and pure Java JDK HttpServer.

The application combines real-time weather forecasting, AI-powered travel itineraries, dynamic packing recommendations, and a multi-category financial budget ledger into a unified platform. It strictly implements a 3-Tier Layered Architecture and operates in dual execution modes: an interactive command-line terminal interface (CLI) and a web application interface powered by a built-in Java HTTP server on port 8080. It requires zero external frameworks (such as Spring or Maven) and uses local CSV files for persistent data storage.

---

## Features

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

## Technologies / Tools Used

| Layer / Category | Technologies Used |
| :--- | :--- |
| **Programming Language** | Pure Java JDK 17+ (Standard OpenJDK / Oracle JDK) |
| **Web Server Engine** | `com.sun.net.httpserver.HttpServer` (Pure JDK 11+) |
| **HTTP Client API** | `java.net.http.HttpClient` (Pure JDK 11+) |
| **External Weather API** | Open-Meteo REST API (Keyless Live Weather Forecasting) |
| **External AI Engine** | Google Gemini AI REST API (`gemini-1.5-flash` / `gemini-2.0-flash`) |
| **Data Persistence** | Human-Readable CSV File I/O (`trips.csv`, `expenses.csv`) |
| **Testing Framework** | Standard Java Unit Test Suite (`VoyageAiTest.java`) |

---

## Steps to Install & Run the Project

### 1. Prerequisites
Ensure you have Java Development Kit (JDK 17 or higher) installed on your system.

### 2. Clone the Repository
```bash
git clone https://github.com/diyaapoulkar-alt/VoyageAI-CLI.git
cd VoyageAI-CLI
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

### 4. Running the Web Application (Port 8080)
To launch the interactive web interface server:

```bash
java -cp bin com.vityarthi.voyageai.WebMain
```

Open your browser and navigate to: **http://localhost:8080**

---

### 5. Running the Command-Line Terminal (CLI) Interface
To run the interactive terminal-based menu application:

```bash
java -cp bin com.vityarthi.voyageai.Main
```

---

## Instructions for Testing

VoyageAI includes a built-in automated unit testing suite (`VoyageAiTest.java`) containing 13 automated unit tests validating trip models, expense categories, CSV serialization/parsing, date formatting, and budget calculation math.

To execute the unit test suite, run:

```bash
java -cp bin com.vityarthi.voyageai.VoyageAiTest
```

### Verified Terminal Test Output:

```text
==================================================
     RUNNING VOYAGEAI AUTOMATED UNIT TESTS        
==================================================
[PASS] Trip ID getter test
[PASS] Trip budget getter test
[PASS] Trip CSV Serialization & Parsing test
[PASS] Expense amount getter test
[PASS] Expense CSV Serialization & Parsing test
[PASS] Valid positive double test
[PASS] Invalid negative double test
[PASS] Valid ISO date format test
[PASS] Invalid date format test
[PASS] Valid expense category test
[PASS] Invalid expense category test
[PASS] WeatherService fallback weather object generation test
[PASS] TripService total spent calculation test

--------------------------------------------------
Test Execution Summary: 13 / 13 Tests Passed.
STATUS: ALL TESTS PASSED SUCCESSFULLY!
--------------------------------------------------
```

---

## Output Screenshots

### Web Interface Dashboard (http://localhost:8080)

```text
+-------------------------------------------------------------------------+
| VoyageAI Travel Planner                                                 |
| AI-Powered Travel Itineraries, Live Weather Forecasts & Budget          |
+-------------------------------------------------------------------------+
| Destination City: [ Iceland  ]  Budget ($): [ 1500 ]  Duration: [ 5 ]   |
| [ Generate Smart Itinerary ]                                            |
+-----------------------------------+-------------------------------------+
| Weather: Iceland (4.7 C, Foggy)   | Recommended Day-by-Day Itinerary    |
| Wind: 7.4 km/h                    | TOP FAMOUS PLACES TO VISIT IN ICELAND|
|                                   |  - Blue Lagoon Geothermal Spa       |
| Weather-Smart Packing Checklist:  |  - Golden Circle                    |
|  - Thermal base layers            |  - Northern Lights Tour             |
|  - Fleece sweater                 |  - Jokulsarlon Glacier Lagoon       |
|  - Waterproof parka jacket        |  - Reynisfjara Black Sand Beach     |
|  - Insulated gloves & boots       |                                     |
+-----------------------------------+-------------------------------------+
```

### Terminal Command-Line Interface (CLI)

```text
==========================================================================
   VoyageAI CLI - AI Smart Travel Planner & Weather Intelligence          
==========================================================================
   Status: Active | Weather API: Open-Meteo | AI Engine: Google Gemini API 
==========================================================================

--- MAIN MENU ---
1. Create New Trip Plan
2. View All Saved Trips
3. Log Trip Expense (Flight, Hotel, Food, Activity)
4. View Trip Budget & Category Expenses
5. Check Real-Time Weather (Open-Meteo API)
6. Generate AI Day-by-Day Itinerary (Gemini AI)
7. Generate Smart Weather Packing Checklist
8. Run Automated System Unit Tests
9. Exit
--------------------------------------------------
```

---

## Project Structure

```text
VoyageAI-CLI/
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
