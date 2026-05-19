package kfu.itis.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kfu.itis.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private static final String CBR_DAILY_JSON_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public CurrencyServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public BigDecimal convertRubToUsd(BigDecimal amountRub) {
        if (amountRub == null || amountRub.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal usdRate = requestUsdRateRub();
        if (usdRate == null || usdRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return amountRub.divide(usdRate, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal requestUsdRateRub() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(CBR_DAILY_JSON_URL)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode usdNode = root.path("Valute").path("USD").path("Value");
            if (usdNode.isMissingNode()) {
                return null;
            }
            return BigDecimal.valueOf(usdNode.asDouble());
        } catch (IOException e) {
            log.error("Ошибка запроса курса валют", e);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос курса валют прерван", e);
            return null;
        }
    }
}