package kfu.itis.model.dto;

public record OrderUpdateRequestDto(
        String title,
        String description,
        String address
) {
}
