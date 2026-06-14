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

@Component
@DgsComponent
public class ButtonDataFetcher {

    private final ButtonService buttonService;

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
            @InputArgument LocalDate subscriptionActivation,
            @InputArgument LocalDate endOfSubscription
    ) {
        try {
            ButtonRequest request = new ButtonRequest(
                    Long.parseLong(userId),
                    Long.parseLong(requestId),
                    null,
                    null,
                    value,
                    visitsHall,
                    subscriptionActivation,
                    endOfSubscription
            );

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

        UpdateButtonRequest request = new UpdateButtonRequest(
                input.getVisitsHall(),
                input.getValue(),
                input.getSubscriptionActivation(),
                input.getEndOfSubscription()
        );

        return buttonService.update(Long.parseLong(requestId), request);
    }

    @DgsMutation(field = "deleteButton")
    public boolean deleteButton(@InputArgument("requestId") String requestId) {
        System.out.println(">>> УДАЛЕНИЕ КНОПКИ! requestId: " + requestId);
        buttonService.delete(Long.parseLong(requestId));
        return true;
    }
}