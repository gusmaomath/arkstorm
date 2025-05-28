package com.fiap.arkstorm.dto;

import java.util.List;

public class WeatherReportDTO {
    private String city;
    private int totalAlerts;
    private List<String> risks;

    public WeatherReportDTO(String city, int totalAlerts, List<String> risks) {
        this.city = city;
        this.totalAlerts = totalAlerts;
        this.risks = risks;
    }

    public String getCity() { return city; }
    public int getTotalAlerts() { return totalAlerts; }
    public List<String> getRisks() { return risks; }
}