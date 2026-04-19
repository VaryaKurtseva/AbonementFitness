package com.example.Abonement_demo_rest.assemblers;

import com.example.AbonementFitness.dto.UserResponse;
import com.example.Abonement_demo_rest.controllers.ButtonController;
import com.example.Abonement_demo_rest.controllers.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {
    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        return EntityModel.of(user,
        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers(user.getFirstName(), user.getLastName(), 0,20)).withRel("collection"),
                linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).getUserByName(user.getFullName(), 0,10)).withRel("searchByName")
        );
    }
}
