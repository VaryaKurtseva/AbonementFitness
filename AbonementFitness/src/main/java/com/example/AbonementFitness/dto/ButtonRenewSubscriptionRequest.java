package com.example.AbonementFitness.dto;

import com.example.AbonementFitness.validation.NoActiveSubscription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Пользователь нажимает на кнопку, для продления абонемента")
public record ButtonRenewSubscriptionRequest(
        @Schema(description = "ID пользователя", example = "1")
        @NotNull(message = "ID пользователя обязателен")
        @NoActiveSubscription
        Long userId,

    @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
    @Max(value = 12, message = "Максимум 12 месяцев")
    @Min(value = 1, message = "Минимум 1 месяц")
    int value,

    @Schema(description = "Дата активации абонемента", example = "2026-01-03")
    LocalDate subscriptionActivation,
    @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
    LocalDate endOfSubscription
) {}
