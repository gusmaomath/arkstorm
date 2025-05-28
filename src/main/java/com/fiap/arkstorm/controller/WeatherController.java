package com.fiap.arkstorm.controller;

import com.fiap.arkstorm.dto.WeatherReportDTO;
import com.fiap.arkstorm.dto.WeatherResponseDTO;
import com.fiap.arkstorm.model.WeatherAlert;
import org.springframework.web.bind.annotation.*;
import com.fiap.arkstorm.service.WeatherService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherResponseDTO getWeatherStatus(@PathVariable String city) {
        WeatherAlert alert = weatherService.fetchAndSaveWeather(city);
        return new WeatherResponseDTO(city, alert.getRiskLevel());
    }

    @GetMapping("/history/{city}")
    public List<WeatherAlert> getWeatherHistory(@PathVariable String city) {
        return weatherService.getWeatherHistory(city);
    }

    @PostMapping("/local")
    public WeatherResponseDTO getRiskByAddress(@RequestBody Map<String, String> payload) {
        String address = payload.get("address");
        return weatherService.getWeatherByAddress(address);
    }

    @GetMapping("/report/{city}")
    public WeatherReportDTO getReport(@PathVariable String city, @RequestParam(defaultValue = "7") int days) {
        return weatherService.getRiskReport(city, days);
    }
}