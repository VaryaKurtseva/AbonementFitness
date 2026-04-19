package com.example.Abonement_demo_rest.graphql.fetcher;

import com.example.AbonementFitness.dto.ButtonResponse;
import com.example.AbonementFitness.dto.PagedResponse;
import com.example.AbonementFitness.dto.UserResponse;
import com.example.Abonement_demo_rest.graphql.types.ButtonConnectionGql;
import com.example.Abonement_demo_rest.graphql.types.PageInfoGql;
import com.example.Abonement_demo_rest.service.ButtonService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;

/**
 * Вложенный резолвер для поля User.buttons.
 *
 * Срабатывает когда клиент запрашивает button user:
 *
 *   query {
 *     user(id: "1") {
 *       firstName
 *       buttons(page: 0, size: 5) {    ← этот резолвер
 *         content {
 *           visitsHall
 *         }
 *         totalElements
 *       }
 *     }
 *   }
 *
 * Демонстрирует работу с аргументами вложенного поля (page, size)
 * и доступ к родительскому объекту (User).
 */
@DgsComponent
public class UserButtonDataFetcher {
    public final ButtonService buttonService;

    public UserButtonDataFetcher(ButtonService buttonService) {
        this.buttonService = buttonService;
    }

    @DgsData(parentType = "User", field = "buttons")
    public ButtonConnectionGql buttons(
         DataFetchingEnvironment dfe,
         @InputArgument Integer page,
         @InputArgument Integer size){
        UserResponse user = dfe.getSource();
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        PagedResponse<ButtonResponse> paged = buttonService.findAll(
                user.getId(), pageNum, pageSize);

        return new ButtonConnectionGql(
                paged.content(),
                new PageInfoGql(paged.pageNumber(),paged.pageSize(), paged.totalPages(), paged.last()),
                (int) paged.totalElements());
    }






}
