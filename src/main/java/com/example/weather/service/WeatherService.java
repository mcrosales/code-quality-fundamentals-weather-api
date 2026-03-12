package com.example.weather.service;

import com.example.weather.model.WeatherData;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private Map<String, WeatherData> weatherCache = new HashMap<>();

    @PostConstruct
    public void init() {
        weatherCache.put("madrid", new WeatherData("Madrid", 28.5, 45.0, "Sunny"));
        weatherCache.put("london", new WeatherData("London", 14.2, 78.0, "Cloudy"));
        weatherCache.put("new york", new WeatherData("New York", 22.1, 60.0, "Partly Cloudy"));
        weatherCache.put("tokyo", new WeatherData("Tokyo", 26.8, 70.0, "Rainy"));
        weatherCache.put("sydney", new WeatherData("Sydney", 19.5, 55.0, "Sunny"));
    }

    public WeatherData getWeather(String city) {
        WeatherData data = weatherCache.get(city.toLowerCase());
        if (data == null) {
            return null;
        }
        logger.info("Fetching weather for: {}", data.getCity());
        return data;
    }

    public List<WeatherData> getAllWeather() {
        return new ArrayList<>(weatherCache.values());
    }

    public String getWeatherAlert(String city) {
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

    public String loadWeatherData(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            logger.error("Failed to load weather data from file: {}", filePath, e);
        }
        return content.toString();
    }

    public void printWeatherReport() {
        logger.info("Weather report - generated at {}", System.currentTimeMillis());
        for (WeatherData data : weatherCache.values()) {
            logger.info("Weather report - {}: {}°C", data.getCity(), data.getTemperatureCelsius());
        }
        logger.info("Weather report - end");
    }

}
