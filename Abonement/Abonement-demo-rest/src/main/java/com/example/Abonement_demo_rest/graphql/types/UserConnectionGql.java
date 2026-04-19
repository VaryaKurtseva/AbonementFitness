package com.example.Abonement_demo_rest.graphql.types;

import com.example.AbonementFitness.dto.UserResponse;

import java.util.List;

public record UserConnectionGql(
        List<UserResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {
}
