package com.example.AbonementFitness.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Данные автора в ответе API.
 *
 * Расширяет RepresentationModel для поддержки HATEOAS-ссылок — поэтому здесь
 * обычный класс с Lombok, а не record (record не может расширять классы).
 * Поля со значением null не попадают в JSON ответа.
 */
@Getter
@Builder

@EqualsAndHashCode(callSuper = false) // не включаем HATEOAS-ссылки в сравнение equals
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "users", itemRelation = "user")
@Schema(description = "Информация об пользователях")
public class UserResponse extends RepresentationModel<UserResponse> {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private final Long id;

    @Schema(description = "Имя пользователя", example = "Лев")
    private final String firstName;

    @Schema(description = "Фамилия пользователя", example = "Толстой")
    private final String lastName;

    @Schema(description = "Полное имя (firstName + lastName)", example = "Лев Толстой")
    private final String fullName;

    @Schema(description = "Email пользователя", example = "tolstoy@example.com")
    private final String email;

    @Schema(description = "Дата активации абонемента", example = "2026-01-03")
    private final LocalDate subscriptionActivation;
    @Schema(description = "Дата окончания абонемента", example = "2026-03-03")
    private final LocalDate  endOfSubscription;


    @Schema(description = "Общее количество посещений зала", example = "3")
    private final Integer visitsHall;
    @Schema(description = "Номер телефона", example = "+79262533595")
    private final String numberPhone;

    public UserResponse(Long id, String firstName, String lastName, String fullName, String email, LocalDate subscriptionActivation, LocalDate endOfSubscription, Integer visitsHall, String numberPhone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.email = email;
        this.subscriptionActivation = subscriptionActivation;
        this.endOfSubscription = endOfSubscription;
        this.visitsHall = visitsHall;
        this.numberPhone = numberPhone;
    }
}
