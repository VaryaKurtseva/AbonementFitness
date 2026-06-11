package com.example.AbonementFitness.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false) // не включаем HATEOAS-ссылки в сравнение equals
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "users", itemRelation = "user")
@Schema(description = "Фильтрация для пользователей")
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {
    String firstName;
    String lastName;
}
