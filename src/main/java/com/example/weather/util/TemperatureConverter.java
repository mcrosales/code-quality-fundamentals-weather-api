package com.example.weather.util;

public class TemperatureConverter {

    private TemperatureConverter() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    private static final double FAHRENHEIT_FACTOR = 1.8;
    private static final double FAHRENHEIT_OFFSET = 32.0;

    private static final double THRESHOLD_FREEZING = 0.0;
    private static final double THRESHOLD_COLD = 15.0;
    private static final double THRESHOLD_MILD = 25.0;
    private static final double THRESHOLD_WARM = 35.0;

    private static final String CLASS_FREEZING = "freezing";
    private static final String CLASS_COLD = "cold";
    private static final String CLASS_MILD = "mild";
    private static final String CLASS_WARM = "warm";
    private static final String CLASS_HOT = "hot";
    private static final String SUFFIX_HUMID = "-humid";
    private static final String SUFFIX_COASTAL = "-coastal";
    private static final String SUFFIX_MOUNTAIN = "-mountain";
    private static final String SUFFIX_TROPICAL = "-tropical";

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
        String base = determineBaseClassification(temp);
        return buildFullClassification(base, isHumid, region);
    }

    private static String determineBaseClassification(double temp) {
        if (temp < THRESHOLD_FREEZING) {
            return CLASS_FREEZING;
        } else if (temp < THRESHOLD_COLD) {
            return CLASS_COLD;
        } else if (temp < THRESHOLD_MILD) {
            return CLASS_MILD;
        } else if (temp < THRESHOLD_WARM) {
            return CLASS_WARM;
        } else {
            return CLASS_HOT;
        }
    }

    private static String buildFullClassification(String base, boolean humid, String region) {
        StringBuilder sb = new StringBuilder(base);
        if (humid) {
            sb.append(SUFFIX_HUMID);
            switch (base) {
                case CLASS_FREEZING:
                    if ("coastal".equals(region)) sb.append(SUFFIX_COASTAL);
                    break;
                case CLASS_COLD:
                    if ("coastal".equals(region)) sb.append(SUFFIX_COASTAL);
                    else if ("mountain".equals(region)) sb.append(SUFFIX_MOUNTAIN);
                    break;
                case CLASS_WARM:
                    if ("tropical".equals(region)) sb.append(SUFFIX_TROPICAL);
                    break;
                case CLASS_HOT:
                    if ("desert".equals(region)) return "extreme-heat";
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

}
