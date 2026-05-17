package kfu.itis.service.impl;

import kfu.itis.model.entity.Specialization;
import kfu.itis.model.entity.User;
import kfu.itis.service.AiOrderSuggestionService;
import kfu.itis.service.SpecializationService;
import kfu.itis.service.UserService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiOrderSuggestionServiceImpl implements AiOrderSuggestionService {

    private final UserService userService;
    private final SpecializationService specializationService;

    public AiOrderSuggestionServiceImpl(UserService userService, SpecializationService specializationService) {
        this.userService = userService;
        this.specializationService = specializationService;
    }

    @Override
    public Map<String, Object> buildSuggestion(String issueDescription, Long specializationId, Double minRating) {
        Specialization specialization = specializationService.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Специализация не найдена"));

        List<User> masters = minRating == null
                ? userService.findAllMastersBySpecialization(specialization)
                : userService.findMastersBySpecializationAndMinRating(specialization, minRating);

        List<String> candidates = masters.stream()
                .limit(5)
                .map(m -> String.format("%s %s (rating %.2f)", m.getFirstName(), m.getLastName(), m.getRating()))
                .toList();

        String prompt = buildPrompt(issueDescription, specialization.getName(), minRating, candidates);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("prompt", prompt);
        result.put("recommendedMasters", candidates);
        result.put("specialization", specialization.getName());
        return result;
    }

    @Override
    public String buildPrompt(String issueDescription, String specializationName, Double minRating, List<String> candidates) {
        return "Ты ассистент платформы бытовых услуг. " +
                "Определи приоритетность и срочность поломки: '" + issueDescription + "'. " +
                "Подбери мастеров по специализации '" + specializationName + "' " +
                "и минимальному рейтингу " + (minRating == null ? "без ограничений" : minRating) + ". " +
                "Кандидаты: " + String.join(", ", candidates) + ". " +
                "Верни краткий ответ: 1) какой мастер лучше, 2) почему, 3) ориентир по сложности работ.";
    }
}
