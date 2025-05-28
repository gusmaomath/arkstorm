package com.fiap.arkstorm.service;

import com.fiap.arkstorm.dto.WeatherReportDTO;
import com.fiap.arkstorm.dto.WeatherResponseDTO;
import com.fiap.arkstorm.model.WeatherAlert;
import com.fiap.arkstorm.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fiap.arkstorm.repository.WeatherAlertRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherAlertRepository repository;
    private static final String API_KEY = "151bfee7cfe094fc333702ae553304b6";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=pt_br";

    public WeatherService(WeatherAlertRepository repository) {
        this.repository = repository;
    }

    public WeatherData getWeatherByCity(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(BASE_URL, city, API_KEY);
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
        double temp = json.getJSONObject("main").getDouble("temp");

        return new WeatherData(city, temp, description);
    }

    public String checkElectricityRisk(WeatherData data) {
        String desc = data.getDescription().toLowerCase();
        if (desc.contains("tempestade") || desc.contains("chuva forte") || desc.contains("ventania")) {
            return "Risco alto de apagão";
        } else {
            return "Sem risco identificado";
        }
    }

    public WeatherAlert fetchAndSaveWeather(String city) {
        WeatherData data = getWeatherByCity(city);
        String risk = checkElectricityRisk(data);
        WeatherAlert alert = new WeatherAlert(data.getCity(), data.getDescription(), risk, LocalDateTime.now());
        return repository.save(alert);
    }

    public List<WeatherAlert> getWeatherHistory(String city) {
        return repository.findByCityIgnoreCase(city);
    }

    public WeatherResponseDTO getWeatherByAddress(String address) {
        String geocodeUrl = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&format=json&limit=1";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(geocodeUrl, String.class);

        JSONArray arr = new JSONArray(response);
        if (arr.length() == 0) {
            return new WeatherResponseDTO(address, "Endereço não encontrado");
        }

        JSONObject location = arr.getJSONObject(0);
        String lat = location.getString("lat");
        String lon = location.getString("lon");

        String weatherUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric&lang=pt_br", lat, lon, API_KEY);
        String weatherResp = restTemplate.getForObject(weatherUrl, String.class);

        JSONObject json = new JSONObject(weatherResp);
        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");

        String risk = checkElectricityRisk(new WeatherData("local", 0, description));
        return new WeatherResponseDTO(location.getString("display_name"), risk);
    }

    public WeatherReportDTO getRiskReport(String city, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<WeatherAlert> alerts = repository.findByCityIgnoreCaseAndTimestampAfter(city, since);
        List<String> risks = alerts.stream().map(WeatherAlert::getRiskLevel).toList();
        return new WeatherReportDTO(city, alerts.size(), risks);
    }
}