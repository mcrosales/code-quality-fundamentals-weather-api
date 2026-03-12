# Weather API

A classic Spring Boot REST API that serves weather data for different cities. This project requires **no external dependencies or API keys** — it runs out of the box with hardcoded data.

## What is a REST API?

A **REST API** (Representational State Transfer) is a way for applications to communicate over HTTP — the same protocol your browser uses to load web pages. Instead of returning HTML pages, a REST API returns **data** (usually JSON).

The key idea is that you use **HTTP methods** to perform operations on **resources** (things like cities, weather data, users, etc.):

| HTTP Method | Purpose | Example |
|---|---|---|
| `GET` | Read/retrieve data | "Give me the weather for Madrid" |
| `POST` | Create new data | "Add a new city" |
| `PUT` | Update existing data | "Update London's temperature" |
| `DELETE` | Remove data | "Remove a city" |

This project only uses `GET` since it's a read-only API.

## How This Project Works

### The architecture

```
HTTP Request                    Spring Boot App
────────────────────────────────────────────────────────────────
                    ┌──────────────────┐     ┌────────────────┐
GET /weather/madrid │ WeatherController │────▶│ WeatherService │──▶ In-memory HashMap
                    │  (REST layer)    │     │ (Business logic)│     with 5 cities
                    └──────────────────┘     └────────────────┘
                              │
                              ▼
                    JSON Response:
                    {
                      "city": "Madrid",
                      "temperatureCelsius": 28.5,
                      "humidity": 45.0,
                      "condition": "Sunny"
                    }
```

The app follows a **layered architecture**, a common pattern in Spring Boot:

| Layer | Class | Responsibility |
|---|---|---|
| **Controller** | `WeatherController.java` | Receives HTTP requests, returns HTTP responses |
| **Service** | `WeatherService.java` | Contains the business logic (fetching data, generating alerts) |
| **Model** | `WeatherData.java` | Defines the data structure (what fields a weather record has) |
| **Utility** | `TemperatureConverter.java` | Helper methods (temperature conversion) |

### Key Spring Boot annotations

```java
@RestController          // Marks this class as a REST API controller
@RequestMapping("/weather")  // All endpoints in this class start with /weather
@GetMapping("/{city}")   // Maps GET requests to this method. {city} is a variable in the URL
@PathVariable String city    // Extracts the {city} value from the URL
@RequestParam String city    // Extracts a query parameter (?city=madrid)
@Service                 // Marks this class as a business logic component
@PostConstruct           // Runs this method automatically after the app starts
```

### Where does the data come from?

This project uses **hardcoded in-memory data** — no database, no external API. When the app starts, `WeatherService.init()` populates a `HashMap` with 5 cities:

```java
@PostConstruct
public void init() {
    weatherCache.put("madrid", new WeatherData("Madrid", 28.5, 45.0, "Sunny"));
    weatherCache.put("london", new WeatherData("London", 14.2, 78.0, "Cloudy"));
    weatherCache.put("new york", new WeatherData("New York", 22.1, 60.0, "Partly Cloudy"));
    weatherCache.put("tokyo", new WeatherData("Tokyo", 26.8, 70.0, "Rainy"));
    weatherCache.put("sydney", new WeatherData("Sydney", 19.5, 55.0, "Sunny"));
}
```

In a real project, this data would come from a database or an external weather API.

## Running the Application

```bash
cd weather-api
mvn spring-boot:run
```

The app starts on **http://localhost:8080**.

## API Endpoints

| Endpoint | Description | Example |
|---|---|---|
| `GET /weather` | List all cities and their weather | `curl http://localhost:8080/weather` |
| `GET /weather/{city}` | Get weather for a specific city | `curl http://localhost:8080/weather/madrid` |
| `GET /weather/alert/{city}` | Get weather alerts for a city | `curl http://localhost:8080/weather/alert/madrid` |
| `GET /weather/search?city=...` | Search for a city (has intentional XSS bug) | `curl "http://localhost:8080/weather/search?city=madrid"` |

### Swagger UI

Once the app is running, open **http://localhost:8080/swagger-ui.html** in your browser to explore and test all endpoints interactively.

## Example Response

```bash
curl http://localhost:8080/weather/madrid
```

```json
{
  "city": "Madrid",
  "temperatureCelsius": 28.5,
  "humidity": 45.0,
  "condition": "Sunny",
  "timestamp": 1710200000000
}
```

## Intentional Code Quality Issues

This project contains **deliberate** bugs and code smells for SonarCloud to detect. These are teaching examples — do **not** write code like this in real projects.

| Category | Rule | File | Issue |
|---|---|---|---|
| Bug | S1206 | `WeatherData.java` | `equals()` overridden without `hashCode()` |
| Bug | S2259 | `WeatherService.java` | Possible null pointer dereference |
| Bug | S2095 | `WeatherService.java` | Resource leak (unclosed `BufferedReader`) |
| Code Smell | S1104 | `WeatherData.java` | Public mutable fields (should be private) |
| Code Smell | S106 | `WeatherService.java` | Using `System.out` instead of a proper logger |
| Code Smell | S1481 | `WeatherService.java` | Unused local variable |
| Code Smell | S108 | `WeatherService.java` | Empty catch block (swallowing exceptions) |
| Code Smell | S1128 | `WeatherController.java` | Unused import (`java.util.Date`) |
| Code Smell | S1118 | `TemperatureConverter.java` | Utility class with public constructor |
| Code Smell | S3776 | `TemperatureConverter.java` | High cognitive complexity (deeply nested `if/else`) |
| Security | S2068 | `WeatherService.java` | Hard-coded API key in source code |
| Vulnerability | S5131 | `WeatherController.java` | Reflected XSS (user input in HTML response) |
