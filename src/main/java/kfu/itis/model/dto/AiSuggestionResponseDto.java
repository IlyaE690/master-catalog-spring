package kfu.itis.model.dto;

import java.util.List;

public record AiSuggestionResponseDto(
        Long specializationId,
        String specializationName,
        List<RecommendedMasterDto> recommendedMasters
) {
}