package com.example.Abonement_demo_rest.assemblers;

import com.example.AbonementFitness.dto.ButtonResponse;
import com.example.Abonement_demo_rest.controllers.ButtonController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ButtonAssembler implements RepresentationModelAssembler<ButtonResponse, EntityModel<ButtonResponse>> {
    @Override
    public EntityModel<ButtonResponse> toModel(ButtonResponse button) {
        EntityModel<ButtonResponse> model = EntityModel.of(button,
                linkTo(methodOn(ButtonController.class).renewSubscription(null)).withRel("renew"),
                linkTo(methodOn(ButtonController.class).buttonRenewResponse(button.getRequestId())).withSelfRel()
        );

        return model;
    }
}
