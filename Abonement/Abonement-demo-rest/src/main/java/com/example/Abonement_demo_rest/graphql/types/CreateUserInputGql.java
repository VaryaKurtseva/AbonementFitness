package com.example.Abonement_demo_rest.graphql.types;

import java.time.LocalDate;

public record CreateUserInputGql(
        String firstName,
        String lastName,
        String email,
        LocalDate subscriptionActivation,
        LocalDate endOfSubscription,
        Integer visitsHall,
        String numberPhone


) { }
