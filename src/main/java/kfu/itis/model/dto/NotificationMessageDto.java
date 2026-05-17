package kfu.itis.model.dto;

import java.time.LocalDateTime;

public record NotificationMessageDto(
        Long id,
        String title,
        String message,
        Long relatedOrderId,
        LocalDateTime createdAt
) {
}
