# Project Problem & Scope Statement — VoyageAI Travel Planner

## 1. Problem Statement
Modern travel planning requires coordinating multiple fragmented resources—such as checking destination weather forecasts, researching landmark attractions, organizing daily travel itineraries, estimating budgets, and creating weather-aware packing lists. 

Travelers frequently face challenges such as unexpected adverse weather, misallocated budgets across trip categories, or missing essential seasonal clothing. Existing travel applications often require heavy frameworks, complex database installations, or costly subscription services.

**VoyageAI Travel Planner** solves this by providing a lightweight, pure Java JDK-powered system that integrates real-time REST API weather data, intelligent AI-assisted itinerary planning, and a local CSV-based financial ledger without requiring external frameworks or database setups.

---

## 2. Scope of the Project
The scope of VoyageAI includes:
* **Global Real-Time Weather Integration**: Fetching live temperature (°C/°F), wind speed, and weather condition summaries via Open-Meteo REST APIs.
* **AI & Rule-Based Itinerary Generation**: Generating day-by-day travel schedules incorporating top famous landmarks via Google Gemini AI API with fallback to an offline landmark database.
* **Dynamic Weather-Smart Packing**: Automatically recommending tailored packing checklists (cold thermal gear for sub-18°C/chilly climates vs. lightweight breathable apparel for warm climates).
* **Trip & Budget Ledger**: Managing trip records, tracking expenses across six standard travel categories (`Flight`, `Hotel`, `Food`, `Activity`, `Transport`, `Misc`), and calculating budget utilization progress.
* **Dual Interface Execution**: Execution via terminal command-line interface (CLI) and pure Java JDK HTTP web server interface (`http://localhost:8080`).
* **Automated System Verification**: A built-in unit testing framework validating core models, calculations, and CSV persistence logic.

---

## 3. Target Users
* **Independent Travelers & Vacation Planners**: Individuals looking for an all-in-one tool to plan day-by-day itineraries and weather-smart packing checklists.
* **Budget-Conscious Travelers**: Users who need to log daily expenses and monitor budget utilization progress across travel categories.
* **Academic & Evaluation Assessors**: Evaluators reviewing a clean 3-Tier Layered Architecture implemented purely in standard Java JDK without third-party frameworks.

---

## 4. High-Level Features
* **Module 1: Weather & Geocoding Engine**: Real-time REST API querying, city coordinate lookup, and offline fallback weather engine.
* **Module 2: AI Itinerary & Packing Assistant**: AI-generated travel itineraries, famous landmark discovery, and weather-aware packing checklists.
* **Module 3: Trip & Financial Ledger**: Trip management, category expense tracking, ASCII budget utilization bars, and CSV file storage.
* **Pure JDK Web Interface**: Interactive single-page web application running on port 8080 powered by `com.sun.net.httpserver.HttpServer`.
* **Automated Unit Testing**: 13/13 passing automated unit tests for crash-free reliability.
