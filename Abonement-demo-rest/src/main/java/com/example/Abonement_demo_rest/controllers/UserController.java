package com.example.Abonement_demo_rest.controllers;

import com.example.AbonementFitness.dto.*;
import com.example.AbonementFitness.endpoints.UsersApi;
import com.example.Abonement_demo_rest.assemblers.UserModelAssembler;
import com.example.Abonement_demo_rest.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements UsersApi {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;


    public UserController(UserService userService,
                          UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @Override
    public EntityModel<UserResponse> getUserById(Long id) {
        return userModelAssembler.toModel(userService.findById(id));
    }

    @Override
    public PagedModel<EntityModel<UserResponse>> getAllUsers(String name, String surname, int page, int size) {
        PagedResponse<UserResponse> paged = userService.findAll(page, size);
        List<EntityModel<UserResponse>> springPage = paged.content().stream()
                .map(userModelAssembler::toModel)
                .toList();
        return PagedModel.of(
                springPage,
                new PagedModel.PageMetadata(
                        paged.pageSize(),
                        paged.pageNumber(),
                        paged.totalElements(),
                        paged.totalPages()
                )
        );
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
        PagedResponse<UserResponse> paged = userService.searchByName(query, page, size);
        List<EntityModel<UserResponse>> springPage = paged.content().stream()
                .map(userModelAssembler::toModel)
                .toList();
        return PagedModel.of(
                springPage,
                new PagedModel.PageMetadata(
                        paged.pageSize(),
                        paged.pageNumber(),
                        paged.totalElements(),
                        paged.totalPages()
                )
        );
    }

    @Override
    public void deleteUser(Long id) {
        userService.delete(id);
    }
}