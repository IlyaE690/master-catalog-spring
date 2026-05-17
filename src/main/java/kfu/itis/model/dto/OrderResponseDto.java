package kfu.itis.model.dto;

import kfu.itis.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        String title,
        String description,
        String address,
        LocalDateTime scheduledDate,
        OrderStatus status,
        BigDecimal price,
        Long customerId,
        String customerUsername,
        Long masterId,
        String masterUsername,
        Long specializationId,
        String specializationName
) {
}
