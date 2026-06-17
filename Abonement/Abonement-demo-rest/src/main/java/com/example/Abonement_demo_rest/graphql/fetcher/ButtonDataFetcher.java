package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.graphql.types.*;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@DgsComponent
public class ButtonDataFetcher {

    private final ButtonService buttonService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public ButtonDataFetcher(ButtonService buttonService) {
        this.buttonService = buttonService;
    }

    @DgsQuery
    public ButtonResponse button(@InputArgument("requestId") String requestId) {
        System.out.println(">>> Запрос button вызван с requestId: " + requestId);
        return buttonService.findButtonById(Long.parseLong(requestId));
    }

    @DgsMutation
    public ButtonResponse createButton(
            @InputArgument String userId,
            @InputArgument String requestId,
            @InputArgument Integer value,
            @InputArgument Integer visitsHall,
            @InputArgument String subscriptionActivation,
            @InputArgument String endOfSubscription) {

        System.out.println(">>> createButton called!");
        System.out.println(">>> userId=" + userId);
        System.out.println(">>> requestId=" + requestId);
        System.out.println(">>> value=" + value);
        System.out.println(">>> visitsHall=" + visitsHall);

        try {
            if (userId == null || userId.isBlank()) {
                throw new IllegalArgumentException("userId обязателен");
            }
            if (requestId == null || requestId.isBlank()) {
                throw new IllegalArgumentException("requestId обязателен");
            }
            if (value == null) {
                throw new IllegalArgumentException("value обязателен");
            }
            if (visitsHall == null) {
                throw new IllegalArgumentException("visitsHall обязателен");
            }

            //Конвертируем String → LocalDate
            LocalDate activationDate = parseDate(subscriptionActivation);
            LocalDate endDate = parseDate(endOfSubscription);

            ButtonRequest request = new ButtonRequest(
                    Long.parseLong(userId),
                    Long.parseLong(requestId),
                    null,
                    null,
                    value,
                    visitsHall,
                    activationDate,   // ← LocalDate
                    endDate           // ← LocalDate
            );

            System.out.println(">>> ButtonRequest created: " + request);
            ButtonResponse response = buttonService.proccessRenewal(request);
            System.out.println(">>> УСПЕХ! requestId: " + response.getRequestId());
            return response;

        } catch (Exception e) {
            System.err.println(">>> ОШИБКА в createButton: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при создании кнопки: " + e.getMessage(), e);
        }
    }

    @DgsMutation(field = "updateButton")
    public ButtonResponse updateButton(
            @InputArgument("requestId") String requestId,
            @InputArgument("input") UpdateButtonInputGql input) {

        System.out.println(">>> ОБНОВЛЕНИЕ КНОПКИ! requestId: " + requestId);

        if (input == null) {
            throw new IllegalArgumentException("input не может быть пустым");
        }
        //Конвертируем String → LocalDate
        LocalDate activationDate = parseDate(input.getSubscriptionActivation());
        LocalDate endDate = parseDate(input.getEndOfSubscription());

        UpdateButtonRequest request = new UpdateButtonRequest(
                input.getVisitsHall(),
                input.getValue(),
                activationDate,   // ← LocalDate
                endDate           // ← LocalDate
        );

        return buttonService.update(Long.parseLong(requestId), request);
    }

    @DgsMutation(field = "deleteButton")
    public boolean deleteButton(@InputArgument("requestId") String requestId) {
        System.out.println(">>> УДАЛЕНИЕ КНОПКИ! requestId: " + requestId);
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("requestId обязателен");
        }
        buttonService.delete(Long.parseLong(requestId));
        return true;
    }

    /**
     * Парсит строку в LocalDate.
     * Если строка null или пустая — возвращает null.
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + dateStr + ". Ожидается формат yyyy-MM-dd");
        }
    }
}