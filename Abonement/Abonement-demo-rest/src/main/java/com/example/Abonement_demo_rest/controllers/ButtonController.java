package com.example.Abonement_demo_rest.controllers;

import com.example.AbonementFitness.dto.ButtonRenewResponse;
import com.example.AbonementFitness.dto.ButtonRenewSubscriptionRequest;
import com.example.AbonementFitness.dto.PatchButtonRequest;
import com.example.AbonementFitness.dto.UserResponse;
import com.example.AbonementFitness.endpoints.ButtonRenewApi;
import com.example.Abonement_demo_rest.assemblers.ButtonAssembler;
import com.example.Abonement_demo_rest.service.ButtonService;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ButtonController implements ButtonRenewApi {
    private final ButtonService buttonService;
    private final ButtonAssembler buttonAssembler;


    public ButtonController(ButtonService buttonService, ButtonAssembler buttonAssembler) {
        this.buttonService = buttonService;
        this.buttonAssembler = buttonAssembler;

    }



    @Override
    public ResponseEntity<EntityModel<ButtonRenewResponse>> renewSubscription(ButtonRenewSubscriptionRequest request) {
        ButtonRenewResponse response = buttonService.proccessRenewal(request);
        EntityModel<ButtonRenewResponse> model = buttonAssembler.toModel(response);
        return ResponseEntity.accepted().body(model);
    }

    @Override
    public ResponseEntity<EntityModel<ButtonRenewResponse>> buttonRenewResponse(Long requestId) {
        ButtonRenewResponse response = buttonService.getAnzByService(requestId);
        EntityModel<ButtonRenewResponse> model = buttonAssembler.toModel(response);
        return ResponseEntity.accepted().body(model);
    }



    @Override
    public EntityModel<ButtonRenewResponse> updateButton(Long id, ButtonRenewSubscriptionRequest request) {
        return buttonAssembler.toModel(buttonService.update(id, request));
    }


    @Override
    public EntityModel<ButtonRenewResponse> patchButton(Long id, PatchButtonRequest request) {
        return buttonAssembler.toModel(buttonService.patchButton(id, request));
    }

    @Override
    public void deleteButton(Long id) {
        buttonService.delete(id);

    }
}
