package com.example.weather.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherDataTest {

    @Test
    void constructorSetsAllFields() {
        WeatherData data = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        assertEquals("Madrid", data.getCity());
        assertEquals(28.5, data.getTemperatureCelsius());
        assertEquals(45.0, data.getHumidity());
        assertEquals("Sunny", data.getCondition());
        assertTrue(data.getTimestamp() > 0);
    }

    @Test
    void settersUpdateFields() {
        WeatherData data = new WeatherData();
        data.setCity("London");
        data.setTemperatureCelsius(14.2);
        data.setHumidity(78.0);
        data.setCondition("Cloudy");
        data.setTimestamp(12345L);
        assertEquals("London", data.getCity());
        assertEquals(14.2, data.getTemperatureCelsius());
        assertEquals(78.0, data.getHumidity());
        assertEquals("Cloudy", data.getCondition());
        assertEquals(12345L, data.getTimestamp());
    }

    @Test
    void equalsSameObject() {
        WeatherData data = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        assertEquals(data, data);
    }

    @Test
    void equalsNull() {
        WeatherData data = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        assertNotEquals(null, data);
    }

    @Test
    void equalsDifferentClass() {
        WeatherData data = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        assertFalse(data.equals("Madrid"));
    }

    @Test
    void equalsSameCity() {
        WeatherData a = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        WeatherData b = new WeatherData("Madrid", 10.0, 20.0, "Rainy");
        assertEquals(a, b);
    }

    @Test
    void equalsDifferentCity() {
        WeatherData a = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        WeatherData b = new WeatherData("London", 14.2, 78.0, "Cloudy");
        assertNotEquals(a, b);
    }

    @Test
    void hashCodeConsistentWithEquals() {
        WeatherData a = new WeatherData("Madrid", 28.5, 45.0, "Sunny");
        WeatherData b = new WeatherData("Madrid", 10.0, 20.0, "Rainy");
        assertEquals(a.hashCode(), b.hashCode());
    }

}
