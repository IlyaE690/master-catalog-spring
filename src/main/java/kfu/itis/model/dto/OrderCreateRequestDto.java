package kfu.itis.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record OrderCreateRequestDto(
        @NotNull(message = "Специализация обязательна")
        Long specializationId,

        Long targetMasterId,

        @NotBlank(message = "Заголовок обязателен")
        @Size(min = 3, max = 150, message = "Заголовок должен быть от 3 до 150 символов")
        String title,

        @NotBlank(message = "Описание обязательно")
        @Size(min = 5, max = 2000, message = "Описание должно быть от 5 до 2000 символов")
        String description,

        @NotBlank(message = "Адрес обязателен")
        @Size(min = 5, max = 500, message = "Введите корректный адрес")
        String address,

        @NotNull(message = "Дата выполнения обязательна")
        @Future(message = "Дата выполнения не может быть в прошлом")
        LocalDateTime scheduledDate
) {
}
