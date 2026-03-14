package com.example.AbonementFitness.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoActiveSubscriptionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoActiveSubscription {
    String message() default "У пользователя есть активный абонемент";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
