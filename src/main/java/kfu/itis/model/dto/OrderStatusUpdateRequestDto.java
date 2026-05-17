package kfu.itis.model.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusUpdateRequestDto(
        @NotBlank String status
) {
}
