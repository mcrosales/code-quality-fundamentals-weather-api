package com.example.weather.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureConverterTest {

    private static final double DELTA = 0.01;

    @Test
    void celsiusToFahrenheit() {
        assertEquals(32.0, TemperatureConverter.celsiusToFahrenheit(0), DELTA);
        assertEquals(212.0, TemperatureConverter.celsiusToFahrenheit(100), DELTA);
    }

    @Test
    void fahrenheitToCelsius() {
        assertEquals(0.0, TemperatureConverter.fahrenheitToCelsius(32), DELTA);
        assertEquals(100.0, TemperatureConverter.fahrenheitToCelsius(212), DELTA);
    }

    @Test
    void classifyInFahrenheitConvertsFirst() {
        // 30F = -1.1C → freezing
        assertEquals("freezing", TemperatureConverter.classifyTemperature(30, "F", false, "other"));
    }

    @Test
    void classifyFreezingDry() {
        assertEquals("freezing", TemperatureConverter.classifyTemperature(-5, "C", false, "other"));
    }

    @Test
    void classifyFreezingHumidCoastal() {
        assertEquals("freezing-humid-coastal", TemperatureConverter.classifyTemperature(-5, "C", true, "coastal"));
    }

    @Test
    void classifyFreezingHumid() {
        assertEquals("freezing-humid", TemperatureConverter.classifyTemperature(-5, "C", true, "other"));
    }

    @Test
    void classifyColdDry() {
        assertEquals("cold", TemperatureConverter.classifyTemperature(10, "C", false, "other"));
    }

    @Test
    void classifyColdHumidCoastal() {
        assertEquals("cold-humid-coastal", TemperatureConverter.classifyTemperature(10, "C", true, "coastal"));
    }

    @Test
    void classifyColdHumidMountain() {
        assertEquals("cold-humid-mountain", TemperatureConverter.classifyTemperature(10, "C", true, "mountain"));
    }

    @Test
    void classifyColdHumid() {
        assertEquals("cold-humid", TemperatureConverter.classifyTemperature(10, "C", true, "other"));
    }

    @Test
    void classifyMildDry() {
        assertEquals("mild", TemperatureConverter.classifyTemperature(20, "C", false, "other"));
    }

    @Test
    void classifyMildHumid() {
        assertEquals("mild-humid", TemperatureConverter.classifyTemperature(20, "C", true, "other"));
    }

    @Test
    void classifyWarmDry() {
        assertEquals("warm", TemperatureConverter.classifyTemperature(30, "C", false, "other"));
    }

    @Test
    void classifyWarmHumidTropical() {
        assertEquals("warm-humid-tropical", TemperatureConverter.classifyTemperature(30, "C", true, "tropical"));
    }

    @Test
    void classifyWarmHumid() {
        assertEquals("warm-humid", TemperatureConverter.classifyTemperature(30, "C", true, "other"));
    }

    @Test
    void classifyHotDry() {
        assertEquals("hot", TemperatureConverter.classifyTemperature(40, "C", false, "other"));
    }

    @Test
    void classifyHotHumidDesert() {
        assertEquals("extreme-heat", TemperatureConverter.classifyTemperature(40, "C", true, "desert"));
    }

    @Test
    void classifyHotHumid() {
        assertEquals("hot-humid", TemperatureConverter.classifyTemperature(40, "C", true, "other"));
    }

}
