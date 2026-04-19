package com.example.AbonementFitness.dto;


import com.example.AbonementFitness.validation.NoActiveSubscription;
import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Пользователь нажимает на кнопку, для продления абонемента")
public record ButtonRequest(
        @Schema(description = "ID пользователя", example = "1")
        @NotNull(message = "ID пользователя обязателен")
                @NoActiveSubscription
        Long userId,

        @Schema(description = "ID запроса", example = "12345")
        @NotNull(message = "ID запроса обязателен")
        Long requestId,

        @Schema(description = "Причина отказа (если есть)")
        String rejectionReason,
        @Schema(description = "Время обработки запроса")
        String  process,
    @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
    @Max(value = 12, message = "Максимум 12 месяцев")
    @Min(value = 1, message = "Минимум 1 месяц")
    Integer value,
    @Schema(description = "Сколько посещений")
            @ValidVisitsHall
    Integer visitsHall,

    @Schema(description = "Дата активации абонемента", example = "2026-01-03")
    LocalDate subscriptionActivation,
    @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
    LocalDate endOfSubscription
) {}
