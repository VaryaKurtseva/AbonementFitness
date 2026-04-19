package com.example.Abonement_demo_rest.graphql.types;

import com.example.AbonementFitness.dto.ButtonResponse;


import java.util.List;

public record ButtonConnectionGql(
        List<ButtonResponse> content,
        PageInfoGql pageInfo,
        int totalElements
) {
}
