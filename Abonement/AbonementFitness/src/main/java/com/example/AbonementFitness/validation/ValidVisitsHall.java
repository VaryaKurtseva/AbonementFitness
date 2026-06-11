package com.example.AbonementFitness.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VisitsHallValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVisitsHall {
    String message() default "Слишком маленькое количество посещений зала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

