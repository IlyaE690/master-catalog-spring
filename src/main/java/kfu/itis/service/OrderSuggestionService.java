package kfu.itis.service;

import kfu.itis.model.dto.AiSuggestionResponseDto;

public interface AiOrderSuggestionService {
    AiSuggestionResponseDto buildSuggestion(String issueDescription, Double minRating);
    AiSuggestionResponseDto buildSuggestion(String issueDescription, Long specializationId, Double minRating);
}
