package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.graphql.types.*;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.example.Abonement_demo_rest.service.UserService;
import com.netflix.graphql.dgs.*;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

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
            if (input.getFirstName() == null || input.getFirstName().isBlank()) {
                throw new IllegalArgumentException("firstName обязателен");
            }
            if (input.getLastName() == null || input.getLastName().isBlank()) {
                throw new IllegalArgumentException("lastName обязателен");
            }
            if (input.getNumberPhone() == null || input.getNumberPhone().isBlank()) {
                throw new IllegalArgumentException("numberPhone обязателен");
            }
            LocalDate activationDate = parseDate(input.getSubscriptionActivation());
            LocalDate endDate = parseDate(input.getEndOfSubscription());
            UserRequest request = new UserRequest(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getEmail(),
                    activationDate,
                    endDate,
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
        if (input.getFirstName() == null || input.getFirstName().isBlank()) {
            throw new IllegalArgumentException("firstName обязателен");
        }
        if (input.getLastName() == null || input.getLastName().isBlank()) {
            throw new IllegalArgumentException("lastName обязателен");
        }
        if (input.getNumberPhone() == null || input.getNumberPhone().isBlank()) {
            throw new IllegalArgumentException("numberPhone обязателен");
        }
        LocalDate activationDate = parseDate(input.getSubscriptionActivation());
        LocalDate endDate = parseDate(input.getEndOfSubscription());
        UpdateUserRequest request = new UpdateUserRequest(
                input.getFirstName(),
                input.getLastName(),
                input.getEmail(),
                activationDate,
                endDate,
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


    /**
     * Парсит строку в LocalDate.
     * Если строка null или пустая — возвращает null.
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            System.err.println(">>> Ошибка парсинга даты: " + dateStr);
            throw new IllegalArgumentException("Некорректный формат даты: " + dateStr + ". Ожидается формат yyyy-MM-dd");
        }
    }





}
