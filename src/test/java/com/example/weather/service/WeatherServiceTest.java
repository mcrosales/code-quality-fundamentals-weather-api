package com.example.weather.service;

import com.example.weather.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherServiceTest {

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService();
        weatherService.init();
    }

    @Test
    void getWeatherReturnsDataForKnownCity() {
        WeatherData data = weatherService.getWeather("Madrid");
        assertNotNull(data);
        assertEquals("Madrid", data.getCity());
    }

    @Test
    void getAllWeatherReturnsAllCities() {
        assertTrue(weatherService.getAllWeather().size() >= 5);
    }

    @Test
    void getWeatherAlertReturnsNoAlertsForMildCity() {
        String alert = weatherService.getWeatherAlert("London");
        assertEquals("No alerts for London", alert);
    }

    @Test
    void getWeatherAlertReturnsCityNotFound() {
        String alert = weatherService.getWeatherAlert("UnknownCity");
        assertEquals("City not found", alert);
    }

    @Test
    void printWeatherReportDoesNotThrow() {
        weatherService.printWeatherReport();
        assertNotNull(weatherService.getAllWeather());
    }

}
