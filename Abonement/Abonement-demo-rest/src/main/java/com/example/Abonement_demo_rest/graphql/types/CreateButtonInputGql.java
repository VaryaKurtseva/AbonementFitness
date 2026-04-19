package com.example.Abonement_demo_rest.graphql.types;

import java.time.LocalDate;

public record CreateButtonInputGql(
        Long userId,
        Long requestId,
        String rejectionReason,
        String  process,
        Integer value,
        Integer visitsHall,
        LocalDate subscriptionActivation,
        LocalDate endOfSubscription
) {
}
