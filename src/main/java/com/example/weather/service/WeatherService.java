package com.example.weather.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.weather.model.WeatherData;

import jakarta.annotation.PostConstruct;

@Service
public class WeatherService {

    // avoid hard-coded credentials; load from environment

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private Map<String, WeatherData> weatherCache = new HashMap<>();

    @PostConstruct
    public void init() {
        weatherCache.put("madrid", new WeatherData("Madrid", 28.5, 45.0, "Sunny"));
        weatherCache.put("london", new WeatherData("London", 14.2, 78.0, "Cloudy"));
        weatherCache.put("new york", new WeatherData("New York", 22.1, 60.0, "Partly Cloudy"));
        weatherCache.put("tokyo", new WeatherData("Tokyo", 26.8, 70.0, "Rainy"));
        weatherCache.put("sydney", new WeatherData("Sydney", 19.5, 55.0, "Sunny"));
    }

    // handle missing entries and use logger instead of System.out
    public WeatherData getWeather(String city) {
        WeatherData data = weatherCache.get(city.toLowerCase());
        if (data == null) {
            log.warn("No weather data for city {}", city);
            return null;
        }
        log.info("Fetching weather for: {}", data.getCity());
        return data;
    }

    public List<WeatherData> getAllWeather() {
        return new ArrayList<>(weatherCache.values());
    }

    private static final double EXTREME_HEAT = 35.0;
    private static final double HEAT_ADVISORY = 30.0;
    private static final double FREEZE_WARNING = 0.0;
    private static final double FROST_ADVISORY = 5.0;

    public String getWeatherAlert(String city) {
        WeatherData data = weatherCache.get(city.toLowerCase());
        if (data == null) {
            return "City not found";
        }

        double temp = data.getTemperatureCelsius();
        if (temp > EXTREME_HEAT) {
            return "EXTREME HEAT WARNING for " + city;
        } else if (temp > HEAT_ADVISORY) {
            return "Heat advisory for " + city;
        } else if (temp < FREEZE_WARNING) {
            return "FREEZE WARNING for " + city;
        } else if (temp < FROST_ADVISORY) {
            return "Frost advisory for " + city;
        }
        return "No alerts for " + city;
    }

    // properly close resources and log failures
    public String loadWeatherData(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            log.error("Failed to load weather data from {}", filePath, e);
        }
        return content.toString();
    }

    private static final String REPORT_PREFIX = "Weather report -";

    public void printWeatherReport() {
        log.info("{} generated at {}", REPORT_PREFIX, System.currentTimeMillis());
        for (WeatherData data : weatherCache.values()) {
            log.info("{} {}: {}°C", REPORT_PREFIX, data.getCity(), data.getTemperatureCelsius());
        }
        log.info("{} end", REPORT_PREFIX);
    }

}
