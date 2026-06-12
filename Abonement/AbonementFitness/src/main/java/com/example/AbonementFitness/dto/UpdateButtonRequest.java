package com.example.AbonementFitness.dto;

import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
/**
 * Запрос для полного обновления кнопки (PUT).
 *
 * Все обязательные поля должны быть переданы.

 * Пользователь кнопки не меняется

 * Пользователя изменить нельзя — для смены пользователя создайте новую кнопку.

 * Для изменения только отдельных полей используйте PATCH (PatchBookRequest).
 */
@Schema(description = "Полное обновление кнопки (PUT). Все обязательные поля должны присутствовать. "
        + "Пользователь кнопки не меняется.")
public record UpdateButtonRequest (
        @Schema(description = "Сколько посещений")
                @ValidVisitsHall
        Integer visitsHall,
        @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Длительность абонемента не может быть пустой")
        @Max(value = 12, message = "Максимум 12 месяцев")
        @Min(value = 1, message = "Минимум 1 месяц")
        Integer value,

        @Schema(description = "Дата активации абонемента", example = "2026-01-03")
        @NotNull(message = "Дата активации не может быть пустой")
        LocalDate subscriptionActivation,

        @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
        @NotNull(message = "Дата окончания абонемента не может быть пустой")

        LocalDate endOfSubscription
){}

