package com.fiap.arkstorm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class WeatherAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String description;
    private String riskLevel;
    private LocalDateTime timestamp;

    public WeatherAlert() {}

    public WeatherAlert(String city, String description, String riskLevel, LocalDateTime timestamp) {
        this.city = city;
        this.description = description;
        this.riskLevel = riskLevel;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getCity() { return city; }
    public String getDescription() { return description; }
    public String getRiskLevel() { return riskLevel; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setCity(String city) { this.city = city; }
    public void setDescription(String description) { this.description = description; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}