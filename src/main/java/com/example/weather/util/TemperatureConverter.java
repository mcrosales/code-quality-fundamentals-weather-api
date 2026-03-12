package com.example.weather.util;

// Sonar: S1118 — utility class should not have a public constructor
public class TemperatureConverter {

    // Sonar: S109 — magic numbers (1.8 and 32 should be named constants)
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 1.8 + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }

    // Sonar: S3776 — cognitive complexity is too high (deeply nested conditions)
    public static String classifyTemperature(double temp, String unit, boolean isHumid, String region) {
        String classification = "";
        if (unit.equals("F")) {
            temp = (temp - 32) / 1.8;
        }
        if (temp < 0) {
            classification = "freezing";
            if (isHumid) {
                classification = "freezing-humid";
                if (region.equals("coastal")) {
                    classification = "freezing-humid-coastal";
                }
            }
        } else if (temp < 15) {
            classification = "cold";
            if (isHumid) {
                classification = "cold-humid";
                if (region.equals("coastal")) {
                    classification = "cold-humid-coastal";
                } else if (region.equals("mountain")) {
                    classification = "cold-humid-mountain";
                }
            }
        } else if (temp < 25) {
            classification = "mild";
            if (isHumid) {
                classification = "mild-humid";
            }
        } else if (temp < 35) {
            classification = "warm";
            if (isHumid) {
                classification = "warm-humid";
                if (region.equals("tropical")) {
                    classification = "warm-humid-tropical";
                }
            }
        } else {
            classification = "hot";
            if (isHumid) {
                classification = "hot-humid";
                if (region.equals("desert")) {
                    classification = "extreme-heat";
                }
            }
        }
        return classification;
    }

}
