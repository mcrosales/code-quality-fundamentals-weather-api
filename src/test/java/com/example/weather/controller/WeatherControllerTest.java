package com.example.weather.controller;

import com.example.weather.model.WeatherData;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherControllerTest {

    private WeatherController controller;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = mock(WeatherService.class);
        controller = new WeatherController(weatherService);
    }

    @Test
    void getWeatherDelegatesToService() {
        WeatherData data = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        when(weatherService.getWeather("madrid")).thenReturn(data);
        assertEquals(data, controller.getWeather("madrid"));
    }

    @Test
    void getAllWeatherDelegatesToService() {
        List<WeatherData> all = List.of(new WeatherData("Madrid", 28.5, 45.0, "Sunny"));
        when(weatherService.getAllWeather()).thenReturn(all);
        assertEquals(1, controller.getAllWeather().size());
    }

    @Test
    void getAlertDelegatesToService() {
        when(weatherService.getWeatherAlert("london")).thenReturn("No alerts for london");
        assertEquals("No alerts for london", controller.getAlert("london"));
    }

    @Test
    void searchCityEscapesHtmlInput() {
        String result = controller.searchCity("<script>alert('xss')</script>");
        assertFalse(result.contains("<script>"));
        assertTrue(result.contains("&lt;script&gt;"));
    }

    @Test
    void searchCityReturnsSafePlainInput() {
        String result = controller.searchCity("madrid");
        assertTrue(result.contains("madrid"));
    }

}
