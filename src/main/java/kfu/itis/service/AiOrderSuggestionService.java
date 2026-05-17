package kfu.itis.service;

import java.util.List;
import java.util.Map;

public interface AiOrderSuggestionService {
    Map<String, Object> buildSuggestion(String issueDescription, Double minRating);

    Map<String, Object> buildSuggestion(String issueDescription, Long specializationId, Double minRating);

    String buildPrompt(String issueDescription, String specializationName, Double minRating, List<String> candidates);
}
