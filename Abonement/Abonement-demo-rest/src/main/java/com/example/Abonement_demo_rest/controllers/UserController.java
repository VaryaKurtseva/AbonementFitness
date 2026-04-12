package com.example.Abonement_demo_rest.controllers;

import com.example.AbonementFitness.dto.*;
import com.example.AbonementFitness.endpoints.UsersApi;
import com.example.Abonement_demo_rest.assemblers.ButtonAssembler;
import com.example.Abonement_demo_rest.assemblers.UserModelAssembler;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.example.Abonement_demo_rest.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {
    private final UserService userService;
    private final ButtonService buttonService;
    private final UserModelAssembler userModelAssembler;
    private final ButtonAssembler buttonAssembler;
    private final PagedResourcesAssembler<UserResponse> pagedUsersAssembler;
    private final PagedResourcesAssembler<ButtonRenewResponse> pagedButtonsAssembler;

    public UserController(UserService userService, ButtonService buttonService, UserModelAssembler userModelAssembler, ButtonAssembler buttonAssembler, PagedResourcesAssembler<UserResponse> pagedUsersAssembler, PagedResourcesAssembler<ButtonRenewResponse> pagedButtonsAssembler) {
        this.userService = userService;
        this.buttonService = buttonService;
        this.userModelAssembler = userModelAssembler;
        this.buttonAssembler = buttonAssembler;
        this.pagedUsersAssembler = pagedUsersAssembler;
        this.pagedButtonsAssembler = pagedButtonsAssembler;
    }


    @Override
    public EntityModel<UserResponse> getUserById(Long id) {
        return userModelAssembler.toModel(userService.findById(id));
    }

    @Override
    public PagedModel<EntityModel<UserResponse>> getAllUsers(Long userId, String name, String surname, int page, int size) {
        PagedResponse<UserResponse> paged = userService.findAll(page, size);
        Page<UserResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedUsersAssembler.toModel(springPage, userModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> createUser(UserRequest request) {
        UserResponse created = userService.create(request);
        EntityModel<UserResponse> model = userModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        return userModelAssembler.toModel(userService.update(id, request));
    }

    @Override
    public EntityModel<UserResponse> patchUser(Long id, PatchUserRequest request) {
        return userModelAssembler.toModel(userService.patchUser(id, request));
    }

    @Override
    public PagedModel<EntityModel<UserResponse>> getUserByName(String query, int page, int size) {
        PagedResponse<UserResponse> paged = userService.searchByName(query,page, size);
        Page<UserResponse> springPage = new PageImpl<>(
                paged.content(),
                PageRequest.of(paged.pageNumber(), paged.pageSize()),
                paged.totalElements()
        );
        return pagedUsersAssembler.toModel(springPage, userModelAssembler);
    }

    @Override
    public void deleteUser(Long id) {
        userService.delete(id);

    }




}
