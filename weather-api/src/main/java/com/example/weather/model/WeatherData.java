package com.example.weather.model;

public class WeatherData {

    // Sonar: S1104 — public mutable fields should be private
    public String city;
    public String condition;

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
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // Sonar: S1206 — "equals" overridden without "hashCode"
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WeatherData other = (WeatherData) obj;
        return city.equals(other.city);
    }

    // hashCode() intentionally missing — Sonar will flag this as a Bug

}
