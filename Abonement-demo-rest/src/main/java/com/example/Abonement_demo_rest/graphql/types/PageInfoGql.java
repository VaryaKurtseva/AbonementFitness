package com.example.Abonement_demo_rest.graphql.types;

public record PageInfoGql(
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean last
) { }
