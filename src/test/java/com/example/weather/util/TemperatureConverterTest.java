package com.example.weather.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    @Test
    void celsiusToFahrenheitAndBack() {
        double c = 0.0;
        double f = TemperatureConverter.celsiusToFahrenheit(c);
        assertEquals(32.0, f, 0.0001);
        assertEquals(c, TemperatureConverter.fahrenheitToCelsius(f), 0.0001);

        c = 100.0;
        f = TemperatureConverter.celsiusToFahrenheit(c);
        assertEquals(212.0, f, 0.0001);
        assertEquals(c, TemperatureConverter.fahrenheitToCelsius(f), 0.0001);
    }

    @Test
    void classifyTemperatureBasicLevels() {
        assertEquals("freezing", TemperatureConverter.classifyTemperature(-5, "C", false, ""));
        assertEquals("cold", TemperatureConverter.classifyTemperature(10, "C", false, ""));
        assertEquals("mild", TemperatureConverter.classifyTemperature(20, "C", false, ""));
        assertEquals("warm", TemperatureConverter.classifyTemperature(30, "C", false, ""));
        assertEquals("hot", TemperatureConverter.classifyTemperature(40, "C", false, ""));
    }

    @Test
    void classificationWithHumidityAndRegions() {
        assertEquals("freezing-humid", TemperatureConverter.classifyTemperature(-5, "C", true, ""));
        assertEquals("freezing-humid-coastal", TemperatureConverter.classifyTemperature(-5, "C", true, "coastal"));
        assertEquals("cold-humid", TemperatureConverter.classifyTemperature(10, "C", true, ""));
        assertEquals("cold-humid-coastal", TemperatureConverter.classifyTemperature(10, "C", true, "coastal"));
        assertEquals("cold-humid-mountain", TemperatureConverter.classifyTemperature(10, "C", true, "mountain"));
        assertEquals("warm-humid", TemperatureConverter.classifyTemperature(30, "C", true, ""));
        assertEquals("warm-humid-tropical", TemperatureConverter.classifyTemperature(30, "C", true, "tropical"));
        assertEquals("hot-humid", TemperatureConverter.classifyTemperature(40, "C", true, ""));
        assertEquals("extreme-heat", TemperatureConverter.classifyTemperature(40, "C", true, "desert"));
    }

    @Test
    void classificationWithFahrenheitUnit() {
        // 32F should be freezing
        assertEquals("freezing", TemperatureConverter.classifyTemperature(32, "F", false, ""));
        // 212F should be hot
        assertEquals("hot", TemperatureConverter.classifyTemperature(212, "F", false, ""));
    }

    @Test
    void classificationBoundaries() {
        assertEquals("freezing", TemperatureConverter.classifyTemperature(0, "C", false, ""));
        assertEquals("cold", TemperatureConverter.classifyTemperature(15, "C", false, ""));
        assertEquals("mild", TemperatureConverter.classifyTemperature(25, "C", false, ""));
        assertEquals("warm", TemperatureConverter.classifyTemperature(35, "C", false, ""));
    }

    @Test
    void utilityClassNotInstantiable() {
        try {
            TemperatureConverter.class.getDeclaredConstructor().newInstance();
            fail("Instantiation should not be allowed");
        } catch (Exception e) {
            assertTrue(e instanceof AssertionError || e.getCause() instanceof AssertionError);
        }
    }
}
