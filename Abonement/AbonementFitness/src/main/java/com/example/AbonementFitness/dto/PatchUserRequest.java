package com.example.AbonementFitness.dto;



import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Запрос для частичного обновления автора (PATCH, семантика JSON Merge Patch).
 *
 * Все поля необязательны. Передайте только то, что нужно изменить.
 * Поля, которые не переданы (null), сервис оставляет без изменений.
 */
@Schema(description = "Частичное обновление автора (PATCH). Передайте только те поля, которые нужно изменить.")
public record PatchUserRequest(

        @Schema(description = "Имя пользователя", example = "Лев", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(max = 100, message = "Имя не может превышать 100 символов")
        String firstName,

        @Schema(description = "Фамилия пользователя", example = "Толстой", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия пользователя не может быть пустой")
        @Size(max = 100, message = "Фамилия не может превышать 100 символов")
        String lastName,

        @Schema(description = "Email пользователя", example = "tolstoy@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Дата активации абонемента", example = "2026-01-03")
        LocalDate subscriptionActivation,
        @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
        LocalDate endOfSubscription,
        @Schema(description = "Количество посещений зала", example = "40")
        @ValidVisitsHall
        Integer visitsHall,
        @Schema(description = "Номер телефона", example = "+79262533595")
        @Size(min = 11, max = 12, message = "Номер телефона должен содержать 11-12 символов")
        String numberPhone
) {}

