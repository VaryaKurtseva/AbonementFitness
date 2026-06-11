package com.example.Abonement_demo_rest.controllers;

import com.example.AbonementFitness.dto.ButtonResponse;
import com.example.AbonementFitness.dto.ButtonRequest;
import com.example.AbonementFitness.dto.PagedResponse;
import com.example.AbonementFitness.dto.PatchButtonRequest;
import com.example.AbonementFitness.endpoints.ButtonApi;
import com.example.Abonement_demo_rest.assemblers.ButtonAssembler;
import com.example.Abonement_demo_rest.service.ButtonService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ButtonController implements ButtonApi {
    private final ButtonService buttonService;
    private final ButtonAssembler buttonAssembler;

    public ButtonController(ButtonService buttonService, ButtonAssembler buttonAssembler) {
        this.buttonService = buttonService;
        this.buttonAssembler = buttonAssembler;
    }

    @Override
    public PagedModel<EntityModel<ButtonResponse>> getAllButtons(Long userId, int page, int size) {
        PagedResponse<ButtonResponse> paged = buttonService.findAll(userId, page, size);

        List<EntityModel<ButtonResponse>> entityModels = paged.content().stream()
                .map(buttonAssembler::toModel)
                .toList();

        return PagedModel.of(
                entityModels,
                new PagedModel.PageMetadata(
                        paged.pageSize(),
                        paged.pageNumber(),
                        paged.totalElements(),
                        paged.totalPages()
                )
        );
    }

    @Override
    public ResponseEntity<EntityModel<ButtonResponse>> renewSubscription(ButtonRequest request) {
        ButtonResponse response = buttonService.proccessRenewal(request);
        EntityModel<ButtonResponse> model = buttonAssembler.toModel(response);
        return ResponseEntity.accepted().body(model);
    }

    @Override
    public ResponseEntity<EntityModel<ButtonResponse>> buttonRenewResponse(Long requestId) {
        ButtonResponse response = buttonService.getAnzByService(requestId);
        EntityModel<ButtonResponse> model = buttonAssembler.toModel(response);
        return ResponseEntity.accepted().body(model);
    }

    @Override
    public EntityModel<ButtonResponse> updateButton(Long id, ButtonRequest request) {
        return buttonAssembler.toModel(buttonService.update(id, request));
    }

    @Override
    public EntityModel<ButtonResponse> patchButton(Long id, PatchButtonRequest request) {
        return buttonAssembler.toModel(buttonService.patchButton(id, request));
    }

    @Override
    public void deleteButton(Long id) {
        buttonService.delete(id);
    }
}