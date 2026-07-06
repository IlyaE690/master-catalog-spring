package kfu.itis.model.dto;

import java.util.List;

public record SuggestionResponseDto(
        Long specializationId,
        String specializationName,
        List<RecommendedMasterDto> recommendedMasters
) {
}