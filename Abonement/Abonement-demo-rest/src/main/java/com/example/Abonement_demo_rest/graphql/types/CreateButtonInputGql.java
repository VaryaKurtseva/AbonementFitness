package com.example.Abonement_demo_rest.graphql.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateButtonInputGql {
    private Long userId;
    private Long requestId;
    private String rejectionReason;
    private String process;
    private Integer value;
    private Integer visitsHall;
    private LocalDate subscriptionActivation;
    private LocalDate endOfSubscription;
}