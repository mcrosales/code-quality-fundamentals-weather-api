package com.example.weather.util;

public class TemperatureConverter {

    private TemperatureConverter() {
    }

    private static final double FAHRENHEIT_FACTOR = 1.8;
    private static final double FAHRENHEIT_OFFSET = 32;

    public static double celsiusToFahrenheit(double celsius) {
        return celsius * FAHRENHEIT_FACTOR + FAHRENHEIT_OFFSET;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - FAHRENHEIT_OFFSET) / FAHRENHEIT_FACTOR;
    }

    public static String classifyTemperature(double temp, String unit, boolean isHumid, String region) {
        if (unit.equals("F")) {
            temp = fahrenheitToCelsius(temp);
        }
        String base = getBaseClassification(temp);
        if (!isHumid) {
            return base;
        }
        return getHumidClassification(base, region);
    }

    private static String getBaseClassification(double temp) {
        if (temp < 0) return "freezing";
        if (temp < 15) return "cold";
        if (temp < 25) return "mild";
        if (temp < 35) return "warm";
        return "hot";
    }

    private static String getHumidClassification(String base, String region) {
        switch (base) {
            case "freezing":
                return region.equals("coastal") ? "freezing-humid-coastal" : "freezing-humid";
            case "cold":
                if (region.equals("coastal")) return "cold-humid-coastal";
                if (region.equals("mountain")) return "cold-humid-mountain";
                return "cold-humid";
            case "mild":
                return "mild-humid";
            case "warm":
                return region.equals("tropical") ? "warm-humid-tropical" : "warm-humid";
            default:
                return region.equals("desert") ? "extreme-heat" : "hot-humid";
        }
    }

}
