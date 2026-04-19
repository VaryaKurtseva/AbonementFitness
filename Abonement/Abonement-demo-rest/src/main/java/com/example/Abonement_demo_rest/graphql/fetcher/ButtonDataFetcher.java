package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.ButtonRequest;
import com.example.AbonementFitness.dto.ButtonResponse;
import com.example.Abonement_demo_rest.graphql.types.CreateButtonInputGql;
import com.example.Abonement_demo_rest.graphql.types.UpdateButtonInputGql;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class ButtonDataFetcher {
    private final ButtonService buttonService;

    public ButtonDataFetcher(ButtonService buttonService) {
        this.buttonService = buttonService;
    }

    @DgsQuery
    public ButtonResponse button(@InputArgument String requestId) {
        return buttonService.findButtonById(Long.parseLong(requestId));
    }

    @DgsMutation
    public ButtonResponse createButton(@InputArgument CreateButtonInputGql input) {
        ButtonRequest request = new ButtonRequest(
                input.userId(),
                input.requestId(),
                input.rejectionReason(),
                input.process(),
                input.value(),
                input.visitsHall(),
                input.subscriptionActivation(),
                input.endOfSubscription()
        );
        return buttonService.proccessRenewal(request);
    }

    @DgsMutation
    public ButtonResponse updateButton(@InputArgument String requestId, @InputArgument UpdateButtonInputGql input) {
        ButtonResponse existing = buttonService.findButtonById(Long.parseLong(requestId));
        ButtonRequest request = new ButtonRequest(
                existing.getUserId(),
                existing.getRequestId(),
                existing.getRejectionReason(),
                existing.getProcess(),
                input.value(),
                input.visitsHall(),
                input.subscriptionActivation(),
                input.endOfSubscription()
        );
        return buttonService.update(Long.parseLong(requestId), request);
    }

    @DgsMutation
    public boolean deleteButton(@InputArgument String requestId) {
        buttonService.delete(Long.parseLong(requestId));
        return true;
    }
}