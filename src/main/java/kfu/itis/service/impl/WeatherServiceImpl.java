package kfu.itis.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kfu.itis.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private static final String WEATHER_URL = "https://api.open-meteo.com/v1/forecast?latitude=55.7887&longitude=49.1221&current=temperature_2m,weather_code,wind_speed_10m";
    private static final Set<Integer> BAD_WEATHER_CODES = Set.of(51,53,55,56,57,61,63,65,66,67,71,73,75,77,80,81,82,85,86,95,96,99);

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public WeatherServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Cacheable(value = "weatherSummary", key = "'kazanCurrent'")
    public String getWeatherSummaryForKazan() {
        JsonNode current = requestCurrentWeather();
        if (current == null) {
            return "Погода временно недоступна";
        }
        double temp = current.path("temperature_2m").asDouble();
        double wind = current.path("wind_speed_10m").asDouble();
        int code = current.path("weather_code").asInt();
        String weatherKind = BAD_WEATHER_CODES.contains(code) ? "плохая" : "нормальная";
        return String.format("Казань: %.1f°C, ветер %.1f м/с, погода %s (код %d)", temp, wind, weatherKind, code);
    }

    @Override
    @Cacheable(value = "weatherCoeff", key = "'outdoorWorkCoeff'")
    public double getWeatherCoefficientForOutdoorWorks() {
        JsonNode current = requestCurrentWeather();
        if (current == null) {
            return 1.0;
        }
        int code = current.path("weather_code").asInt();
        return BAD_WEATHER_CODES.contains(code) ? 1.25 : 1.0;
    }

    @Override
    @Cacheable(value = "weatherBad", key = "'badWeatherNow'")
    public boolean isBadWeatherNow() {
        return getWeatherCoefficientForOutdoorWorks() > 1.0;
    }

    private JsonNode requestCurrentWeather() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(WEATHER_URL)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode current = root.path("current");
            return current.isMissingNode() ? null : current;
        } catch (IOException e) {
            log.error("Ошибка запроса погоды", e);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос погоды прерван", e);
            return null;
        }
    }
}
