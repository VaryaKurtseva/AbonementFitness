package com.example.AbonementFitness.dto;

import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
/**
 * Запрос для полного обновления книги (PUT).
 *
 * Все обязательные поля должны быть переданы.
 * Автора изменить нельзя — для смены автора создайте новую книгу.
 * Для изменения только отдельных полей используйте PATCH (PatchBookRequest).
 */
@Schema(description = "Полное обновление кнопки (PUT). Все обязательные поля должны присутствовать. "
        + "Пользователь кнопки не меняется.")
public record UpdateButtonRequest (
        @Schema(description = "Сколько посещений")
        @NotBlank(message = "Количество посещений не может быть пустым")
                @ValidVisitsHall
        Integer visitsHall,
        @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Длительность абонемента не может быть пустой")
        @Max(value = 12, message = "Максимум 12 месяцев")
        @Min(value = 1, message = "Минимум 1 месяц")
        Integer value,

        @Schema(description = "Дата активации абонемента", example = "2026-01-03")
        @NotBlank(message = "Дата активации абонемента не может быть пустой")
        LocalDate subscriptionActivation,
        @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
        @NotBlank(message = "Дата окончания абонемента не может быть пустой")
        LocalDate endOfSubscription
){}

