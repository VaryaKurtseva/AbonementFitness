package com.example.AbonementFitness.dto;




import com.example.AbonementFitness.validation.ValidVisitsHall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Запрос для полного обновления пользователя (PUT).
 *
 * Все обязательные поля должны быть переданы.

 * Для изменения только отдельных полей используйте PATCH (PatchUserRequest).
 */
@Schema(description = "Полное обновление пользователя (PUT). Все обязательные поля должны присутствовать. ")
public record UpdateUserRequest(

        @Schema(description = "Имя пользователя", example = "Антон", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(max = 100, message = "Имя пользователя не может превышать 100 символов")
        String firstName,
        @Schema(description = "Фамилия пользователя", example = "Антон", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Фамилия пользователя не может быть пустым")
        @Size(max = 100, message = "Фамилия пользователя не может превышать 100 символов")
        String lastName,
        @Schema(description = "Email пользователя", example = "tolstoy@example.com")
        @Email(message = "Некорректный формат email")
        @Size(max = 255, message = "Email не может превышать 255 символов")
        String email,

        @Schema(description = "Дата активации абонемента")
        LocalDate subscriptionActivation,
        @Schema(description = "Дата окончания абонемента")
        LocalDate endOfSubscription,
        @Schema(description = "Количество посещений ")
        @ValidVisitsHall
        Integer visitsHall,


        @Schema(description = "Номер телефона", example = "+79262533595")
        @Size(min = 11, max = 12, message = "Номер телефона должен содержать 11-12 символов")
        String numberPhone


) {}
