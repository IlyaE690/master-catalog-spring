package kfu.itis.service;

import java.time.LocalDateTime;

public interface WeatherService {
    String getWeatherSummaryForKazan();

    double getWeatherCoefficientForOutdoorWorks(LocalDateTime dateTime);

    boolean isBadWeather(LocalDateTime dateTime);
}