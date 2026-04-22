package com.example.Abonement_demo_rest.graphql.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInputGql {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate subscriptionActivation;
    private LocalDate endOfSubscription;
    private Integer visitsHall;
    private String numberPhone;
}