package kfu.itis.service;

public interface WeatherService {
    String getWeatherSummaryForKazan();

    double getWeatherCoefficientForOutdoorWorks();

    boolean isBadWeatherNow();
}
