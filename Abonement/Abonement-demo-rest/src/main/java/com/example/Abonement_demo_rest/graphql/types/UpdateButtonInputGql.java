package com.example.Abonement_demo_rest.graphql.types;

import java.time.LocalDate;

public record UpdateButtonInputGql(
        Integer visitsHall,
        Integer value,
        LocalDate subscriptionActivation,
        LocalDate endOfSubscription
) {
}
