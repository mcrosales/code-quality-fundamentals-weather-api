package com.example.weather.service;

import com.example.weather.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void getWeatherReturnsNullForUnknownCity() {
        assertNull(weatherService.getWeather("UnknownCity"));
    }

    @Test
    void getAllWeatherReturnsAllCities() {
        assertTrue(weatherService.getAllWeather().size() >= 5);
    }

    @Test
    void getWeatherAlertReturnsNoAlertsForMildCity() {
        assertEquals("No alerts for London", weatherService.getWeatherAlert("London"));
    }

    @Test
    void getWeatherAlertReturnsCityNotFound() {
        assertEquals("City not found", weatherService.getWeatherAlert("UnknownCity"));
    }

    @Test
    void getWeatherAlertReturnsExtremeHeat() throws Exception {
        putInCache("hotcity", new WeatherData("hotcity", 40.0, 30.0, "Sunny"));
        assertEquals("EXTREME HEAT WARNING for hotcity", weatherService.getWeatherAlert("hotcity"));
    }

    @Test
    void getWeatherAlertReturnsHeatAdvisory() throws Exception {
        putInCache("warmcity", new WeatherData("warmcity", 32.0, 30.0, "Sunny"));
        assertEquals("Heat advisory for warmcity", weatherService.getWeatherAlert("warmcity"));
    }

    @Test
    void getWeatherAlertReturnsFreezeWarning() throws Exception {
        putInCache("freezecity", new WeatherData("freezecity", -5.0, 30.0, "Snowy"));
        assertEquals("FREEZE WARNING for freezecity", weatherService.getWeatherAlert("freezecity"));
    }

    @Test
    void getWeatherAlertReturnsFrostAdvisory() throws Exception {
        putInCache("frostcity", new WeatherData("frostcity", 2.0, 30.0, "Cold"));
        assertEquals("Frost advisory for frostcity", weatherService.getWeatherAlert("frostcity"));
    }

    @Test
    void loadWeatherDataReturnsEmptyForNonExistentFile() {
        assertEquals("", weatherService.loadWeatherData("/non/existent/file.txt"));
    }

    @Test
    void loadWeatherDataReadsFileContent() throws Exception {
        File tempFile = Files.createTempFile("weather", ".txt").toFile();
        Files.writeString(tempFile.toPath(), "sunny data");
        assertEquals("sunny data", weatherService.loadWeatherData(tempFile.getAbsolutePath()));
        tempFile.delete();
    }

    @Test
    void printWeatherReportDoesNotThrow() {
        weatherService.printWeatherReport();
        assertNotNull(weatherService.getAllWeather());
    }

    @SuppressWarnings("unchecked")
    private void putInCache(String key, WeatherData data) throws Exception {
        Field field = WeatherService.class.getDeclaredField("weatherCache");
        field.setAccessible(true);
        ((Map<String, WeatherData>) field.get(weatherService)).put(key, data);
    }

}
