package com.fiap.arkstorm.repository;

import com.fiap.arkstorm.model.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, Long> {
    List<WeatherAlert> findByCityIgnoreCase(String city);
    List<WeatherAlert> findByCityIgnoreCaseAndTimestampAfter(String city, LocalDateTime timestamp);
}