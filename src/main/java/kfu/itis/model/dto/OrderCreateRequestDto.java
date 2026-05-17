package kfu.itis.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrderCreateRequestDto(
        @NotNull Long specializationId,
        Long targetMasterId,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String address,
        @NotNull @Future LocalDateTime scheduledDate
) {
}
