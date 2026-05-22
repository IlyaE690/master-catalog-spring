package kfu.itis.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kfu.itis.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${app.external-api.weather-url}")
    private String WEATHER_URL;

    private static final Set<Integer> BAD_WEATHER_CODES = Set.of(
            51,53,55,56,57,61,63,65,66,67,71,73,75,77,80,81,82,85,86,95,96,99
    );

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
    @Cacheable(value = "weatherCoeff", key = "#dateTime.toString()")
    public double getWeatherCoefficientForOutdoorWorks(LocalDateTime dateTime) {
        JsonNode forecast = requestWeatherForDate(dateTime);
        if (forecast == null) {
            return 1.0;
        }
        int code = forecast.path("weather_code").asInt();
        return BAD_WEATHER_CODES.contains(code) ? 1.25 : 1.0;
    }

    @Override
    @Cacheable(value = "weatherBad", key = "#dateTime.toString()")
    public boolean isBadWeather(LocalDateTime dateTime) {
        return getWeatherCoefficientForOutdoorWorks(dateTime) > 1.0;
    }

    private JsonNode requestCurrentWeather() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(WEATHER_URL)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode current = root.path("current");
            return current.isMissingNode() ? null : current;
        } catch (IOException e) {
            log.error("Ошибка запроса текущей погоды", e);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос текущей погоды прерван", e);
            return null;
        }
    }

    private JsonNode requestWeatherForDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=55.7887&longitude=49.1221&daily=weather_code&timezone=Europe/Moscow&start_date=%s&end_date=%s",
                formattedDate, formattedDate
        );

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode daily = root.path("daily");
            JsonNode weatherCodes = daily.path("weather_code");
            if (weatherCodes.isArray() && weatherCodes.size() > 0) {
                int code = weatherCodes.get(0).asInt();
                return new com.fasterxml.jackson.databind.node.ObjectNode(
                        com.fasterxml.jackson.databind.node.JsonNodeFactory.instance
                ).put("weather_code", code);
            }
            return null;
        } catch (IOException e) {
            log.error("Ошибка запроса прогноза погоды для даты {}", formattedDate, e);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос прогноза погоды прерван", e);
            return null;
        }
    }
}
