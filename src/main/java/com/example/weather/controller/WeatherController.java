package com.example.weather.controller;

import com.example.weather.model.WeatherData;
import com.example.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.util.HtmlUtils;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherData getWeather(@PathVariable String city) {
        return weatherService.getWeather(city);
    }

    @GetMapping
    public List<WeatherData> getAllWeather() {
        return weatherService.getAllWeather();
    }

    @GetMapping("/alert/{city}")
    public String getAlert(@PathVariable String city) {
        return weatherService.getWeatherAlert(city);
    }

    // Sonar: S5131 — escape user input to avoid XSS
    @GetMapping("/search")
    public String searchCity(@RequestParam String city) {
        String safeCity = HtmlUtils.htmlEscape(city);
        return "<html><body>Weather search results for: " + safeCity + "</body></html>";
    }

}
