package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.graphql.types.CreateUserInputGql;
import com.example.Abonement_demo_rest.graphql.types.PageInfoGql;
import com.example.Abonement_demo_rest.graphql.types.UpdateUserInputGql;
import com.example.Abonement_demo_rest.graphql.types.UserConnectionGql;
import com.example.Abonement_demo_rest.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

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
    @DgsMutation
    public UserResponse createUser(@InputArgument CreateUserInputGql input){
        UserRequest request = new UserRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.subscriptionActivation(),
                input.endOfSubscription(),
                input.visitsHall(),
                input.numberPhone()
        );
        return userService.create(request);

    }
    @DgsMutation
    public UserResponse updateUser(@InputArgument String id, @InputArgument UpdateUserInputGql input){
        UpdateUserRequest request = new UpdateUserRequest(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.subscriptionActivation(),
                input.endOfSubscription(),
                input.visitsHall(),
                input.numberPhone()
        );
        return userService.update(Long.parseLong(id), request);
    }
    @DgsMutation
    public boolean deleteUser(@InputArgument String id){
        userService.delete(Long.parseLong(id));
        return true;
    }



}
