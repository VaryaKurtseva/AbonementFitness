package com.example.AbonementFitness.dto;


import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


import java.time.LocalDate;

@Schema(description = "Частичное обновление кнопки для предления абонемента (PATCH). Передайте только те поля, которые нужно изменить.")
public record PatchButtonRequest (


        @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
        @Max(value = 12, message = "Максимум 12 месяцев")
        @Min(value = 1, message = "Минимум 1 месяц")
        Integer value,
        @Schema(description = "Количество посещений")
        @ValidVisitsHall
        Integer visitsHall,

        @Schema(description = "Дата активации абонемента", example = "2026-01-03")
        LocalDate subscriptionActivation,
        @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
        LocalDate endOfSubscription
) {}



