package com.example.Abonement_demo_rest.graphql.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateButtonInputGql {
    private Integer visitsHall;
    private Integer value;
    private LocalDate subscriptionActivation;
    private LocalDate endOfSubscription;
}