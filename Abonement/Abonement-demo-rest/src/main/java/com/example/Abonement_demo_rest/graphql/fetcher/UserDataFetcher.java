package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.graphql.types.*;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.example.Abonement_demo_rest.service.UserService;
import com.netflix.graphql.dgs.*;

/**
 * DataFetcher для операций с user.
 *
 * Обрабатывает корневые поля Query и Mutation, связанные с user.
 * Вложенные поля (User.buttons) обрабатываются в UserButtonDataFetcher.
 *
 * Принцип разделения: один DataFetcher — одна группа связанных операций.
 * Это делает код более читаемым и тестируемым.
 */
@DgsComponent
public class UserDataFetcher {
    private final UserService userService;



    public UserDataFetcher(UserService userService) {
        this.userService = userService;


    }
    @DgsQuery
    public UserResponse user(@InputArgument("userId") String userId) {
        return userService.findById(Long.parseLong(userId));
    }
    @DgsQuery
    public UserConnectionGql users(
            @InputArgument UserFilter filter,
            @InputArgument Integer page,
            @InputArgument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PagedResponse<UserResponse> paged = userService.findAll(pageNum, pageSize);
        return new UserConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(), paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements()
        );

    }
    @DgsMutation(field = "createUser")
    public UserResponse createUser(@InputArgument("input") CreateUserInputGql input) {
        System.out.println(">>> createUser called!");
        System.out.println(">>> input: " + input);

        try {
            UserRequest request = new UserRequest(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getEmail(),
                    input.getSubscriptionActivation(),
                    input.getEndOfSubscription(),
                    input.getVisitsHall(),
                    input.getNumberPhone()
            );
            System.out.println(">>> UserRequest created");

            UserResponse response = userService.create(request);
            System.out.println(">>> SUCCESS! id: " + response.getId());
            return response;
        } catch (Exception e) {
            System.err.println(">>> ERROR in createUser!");
            e.printStackTrace();
            throw e;
        }
    }
    @DgsMutation
    public UserResponse updateUser(@InputArgument String id, @InputArgument UpdateUserInputGql input){
        UpdateUserRequest request = new UpdateUserRequest(
                input.getFirstName(),
                input.getLastName(),
                input.getEmail(),
                input.getSubscriptionActivation(),
                input.getEndOfSubscription(),
                input.getVisitsHall(),
                input.getNumberPhone()
        );
        return userService.update(Long.parseLong(id), request);
    }
    @DgsMutation
    public boolean deleteUser(@InputArgument String id){
        userService.delete(Long.parseLong(id));
        return true;
    }





}
