package com.example.AbonementFitness.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO для создания или полного обновления пользователя (POST / PUT).
 * Все обязательные поля должны присутствовать.
 */
@Schema(description = "Запрос на создание или полное обновление пользователя")
public record UserRequest(

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
        LocalDate endOfSubscription


) {}