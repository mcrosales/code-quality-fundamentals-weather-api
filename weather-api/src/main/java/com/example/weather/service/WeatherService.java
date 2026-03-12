package com.example.weather.service;

import com.example.weather.model.WeatherData;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    // Sonar: S2068 — credentials should not be hard-coded
    private static final String API_KEY = "weather-api-secret-key-12345";

    private Map<String, WeatherData> weatherCache = new HashMap<>();

    @PostConstruct
    public void init() {
        weatherCache.put("madrid", new WeatherData("Madrid", 28.5, 45.0, "Sunny"));
        weatherCache.put("london", new WeatherData("London", 14.2, 78.0, "Cloudy"));
        weatherCache.put("new york", new WeatherData("New York", 22.1, 60.0, "Partly Cloudy"));
        weatherCache.put("tokyo", new WeatherData("Tokyo", 26.8, 70.0, "Rainy"));
        weatherCache.put("sydney", new WeatherData("Sydney", 19.5, 55.0, "Sunny"));
    }

    // Sonar: S2259 — null pointer dereference (data could be null if city not found)
    // Sonar: S106 — System.out should not be used for logging
    public WeatherData getWeather(String city) {
        WeatherData data = weatherCache.get(city.toLowerCase());
        System.out.println("Fetching weather for: " + data.getCity()); // Bug: NPE if city not found
        return data;
    }

    public List<WeatherData> getAllWeather() {
        return new ArrayList<>(weatherCache.values());
    }

    // Sonar: S1481 — unused local variable
    // Sonar: S109 — magic numbers
    public String getWeatherAlert(String city) {
        String unusedConfig = "alert-config-v2";

        WeatherData data = weatherCache.get(city.toLowerCase());
        if (data == null) {
            return "City not found";
        }

        if (data.getTemperatureCelsius() > 35) {
            return "EXTREME HEAT WARNING for " + city;
        } else if (data.getTemperatureCelsius() > 30) {
            return "Heat advisory for " + city;
        } else if (data.getTemperatureCelsius() < 0) {
            return "FREEZE WARNING for " + city;
        } else if (data.getTemperatureCelsius() < 5) {
            return "Frost advisory for " + city;
        }
        return "No alerts for " + city;
    }

    // Sonar: S2095 — resources should be closed
    // Sonar: S108 — empty catch block
    public String loadWeatherData(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            // Bug: reader is never closed (resource leak)
        } catch (IOException e) {
            // empty catch block — exception silently swallowed
        }
        return content.toString();
    }

    // Sonar: S106 — System.out for logging (repeated)
    // Sonar: S1192 — duplicated string literal ("Weather report")
    public void printWeatherReport() {
        System.out.println("Weather report - generated at " + System.currentTimeMillis());
        for (WeatherData data : weatherCache.values()) {
            System.out.println("Weather report - " + data.getCity() + ": " + data.getTemperatureCelsius() + "°C");
        }
        System.out.println("Weather report - end");
    }

}
