package com.example.weather.util;

public class TemperatureConverter {

    private TemperatureConverter() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    private static final double FAHRENHEIT_FACTOR = 1.8;
    private static final double FAHRENHEIT_OFFSET = 32.0;

    public static double celsiusToFahrenheit(double celsius) {
        return celsius * FAHRENHEIT_FACTOR + FAHRENHEIT_OFFSET;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - FAHRENHEIT_OFFSET) / FAHRENHEIT_FACTOR;
    }

    public static String classifyTemperature(double temp, String unit, boolean isHumid, String region) {
        if ("F".equals(unit)) {
            temp = fahrenheitToCelsius(temp);
        }

        String base;
        if (temp < 0) {
            base = "freezing";
        } else if (temp < 15) {
            base = "cold";
        } else if (temp < 25) {
            base = "mild";
        } else if (temp < 35) {
            base = "warm";
        } else {
            base = "hot";
        }

        StringBuilder sb = new StringBuilder(base);
        if (isHumid) {
            sb.append("-humid");
            switch (base) {
                case "freezing":
                    if ("coastal".equals(region)) sb.append("-coastal");
                    break;
                case "cold":
                    if ("coastal".equals(region)) sb.append("-coastal");
                    else if ("mountain".equals(region)) sb.append("-mountain");
                    break;
                case "warm":
                    if ("tropical".equals(region)) sb.append("-tropical");
                    break;
                case "hot":
                    if ("desert".equals(region)) return "extreme-heat";
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

}
