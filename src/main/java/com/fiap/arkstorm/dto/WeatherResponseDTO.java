package com.fiap.arkstorm.dto;

public class WeatherResponseDTO {
    private String city;
    private String status;

    public WeatherResponseDTO(String city, String status) {
        this.city = city;
        this.status = status;
    }

    public String getCity() { return city; }
    public String getStatus() { return status; }
}