package com.example.weather.model;

public class WeatherData {

    // Sonar: S1104 — public mutable fields should be private
    private String city;
    private String condition;

    private double temperatureCelsius;
    private double humidity;
    private long timestamp;

    public WeatherData() {
    }

    public WeatherData(String city, double temperatureCelsius, double humidity, String condition) {
        this.city = city;
        this.temperatureCelsius = temperatureCelsius;
        this.humidity = humidity;
        this.condition = condition;
        this.timestamp = System.currentTimeMillis();
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public long getTimestamp() { return timestamp; }
    // timestamp is set at creation and should not be modified

    // Sonar: S1206 — "equals" overridden without "hashCode"
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WeatherData other = (WeatherData) obj;
        return city != null ? city.equals(other.city) : other.city == null;
    }

    @Override
    public int hashCode() {
        return city != null ? city.hashCode() : 0;
    }
}
