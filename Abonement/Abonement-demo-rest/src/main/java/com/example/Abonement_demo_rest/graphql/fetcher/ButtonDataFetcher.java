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

    // ========== QUERIES ==========

    @DgsQuery
    public ButtonResponse button(@InputArgument("requestId") String requestId) {
        System.out.println(">>> Query.button called with requestId: " + requestId);
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
        System.out.println(">>> CREATE BUTTON CALLED!");
        System.out.println(">>> userId: " + userId);
        System.out.println(">>> requestId: " + requestId);

        try {
            ButtonRequest request = new ButtonRequest(
                    Long.parseLong(userId),
                    Long.parseLong(requestId),
                    null,  // rejectionReason
                    null,  // process
                    value,
                    visitsHall,
                    subscriptionActivation,
                    endOfSubscription
            );

            ButtonResponse response = buttonService.proccessRenewal(request);
            System.out.println(">>> SUCCESS! requestId: " + response.getRequestId());
            return response;

        } catch (Exception e) {
            System.err.println(">>> ERROR in createButton: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при создании кнопки: " + e.getMessage(), e);
        }
    }

    @DgsMutation(field = "updateButton")
    public ButtonResponse updateButton(
            @InputArgument("requestId") String requestId,
            @InputArgument("input") UpdateButtonInputGql input) {

        System.out.println(">>> UPDATE BUTTON CALLED! requestId: " + requestId);

        ButtonResponse existing = buttonService.findButtonById(Long.parseLong(requestId));

        ButtonRequest request = new ButtonRequest(
                existing.getUserId(),
                existing.getRequestId(),
                existing.getRejectionReason(),
                existing.getProcess(),
                input.getValue(),
                input.getVisitsHall(),
                input.getSubscriptionActivation(),
                input.getEndOfSubscription()
        );

        return buttonService.update(Long.parseLong(requestId), request);
    }

    @DgsMutation(field = "deleteButton")
    public boolean deleteButton(@InputArgument("requestId") String requestId) {
        System.out.println(">>> DELETE BUTTON CALLED! requestId: " + requestId);
        buttonService.delete(Long.parseLong(requestId));
        return true;
    }
}