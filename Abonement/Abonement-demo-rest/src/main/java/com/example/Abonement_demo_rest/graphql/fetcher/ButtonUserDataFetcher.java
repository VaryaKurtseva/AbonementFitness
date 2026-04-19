package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.ButtonResponse;
import com.example.AbonementFitness.dto.UserResponse;
import com.example.Abonement_demo_rest.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;

@DgsComponent
public class ButtonUserDataFetcher {
    private final UserService userService;


    public ButtonUserDataFetcher(UserService userService) {
        this.userService = userService;
    }
    @DgsData(parentType = "Button", field = "user")
    public UserResponse user(DataFetchingEnvironment dfe) {
        ButtonResponse button = dfe.getSource();

        // Если автор уже вложен в BookResponse, возвращаем его напрямую.
        // В реальном приложении здесь мог бы быть вызов authorService.findById().
        if (button.getUserId() != null) {
            return userService.findById(button.getUserId());
        }

        // Запасной вариант — загрузить автора отдельно (для демонстрации)
        return null;
    }

}
