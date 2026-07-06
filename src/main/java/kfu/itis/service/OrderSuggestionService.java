package kfu.itis.service;

import kfu.itis.model.dto.SuggestionResponseDto;

public interface OrderSuggestionService {
    SuggestionResponseDto buildSuggestion(String issueDescription, Double minRating);
    SuggestionResponseDto buildSuggestion(String issueDescription, Long specializationId, Double minRating);
}
