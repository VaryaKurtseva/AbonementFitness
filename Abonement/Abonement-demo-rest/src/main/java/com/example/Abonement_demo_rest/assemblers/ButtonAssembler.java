package com.example.Abonement_demo_rest.assemblers;

import com.example.AbonementFitness.dto.ButtonRenewResponse;
import com.example.Abonement_demo_rest.controllers.ButtonController;
import com.example.Abonement_demo_rest.controllers.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ButtonAssembler implements RepresentationModelAssembler<ButtonRenewResponse, EntityModel<ButtonRenewResponse>> {
    @Override
    public EntityModel<ButtonRenewResponse> toModel(ButtonRenewResponse button) {
        EntityModel<ButtonRenewResponse> model = EntityModel.of(button,
                linkTo(methodOn(ButtonController.class).renewSubscription(null)).withRel("renew"),
                linkTo(methodOn(ButtonController.class).buttonRenewResponse(button.getRequestId())).withSelfRel()
        );

        return model;
    }
}
