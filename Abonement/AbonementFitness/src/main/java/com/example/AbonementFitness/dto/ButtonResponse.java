package com.example.AbonementFitness.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false) // не включаем HATEOAS-ссылки в сравнение equals
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "button", itemRelation = "button")
@Schema(description = "Результат запроса на продление абонемента")
public class ButtonResponse {
    @Schema(description = "ID пользователя", example = "1")
    Long userId;
    @Schema(description = "ID запроса", example = "12345")
    private final Long requestId;
    @Schema(description = "Причина отказа (если есть)")
    private final String rejectionReason;
    @Schema(description = "Время обработки запроса")
    private final String  process;
    @Schema(description = "На сколько месяцев абонемент",  requiredMode = Schema.RequiredMode.REQUIRED)
    @Max(value = 12, message = "Максимум 12 месяцев")
    @Min(value = 1, message = "Минимум 1 месяц")
    private final Integer value;
    @Schema(description = "Сколько посещений")
    private final Integer visitsHall;

    @Schema(description = "Дата активации абонемента", example = "2026-01-03")
    private final LocalDate subscriptionActivation;
    @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
    private final LocalDate endOfSubscription;


}
