package com.example.AbonementFitness.validation;

import com.example.AbonementFitness.exception.ApiButtonExceptionUserId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoActiveSubscriptionValidator implements ConstraintValidator<NoActiveSubscription, Long> {



    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        if (userId == null) {
            throw new RuntimeException("Пользователь отсутствует");
        }


        return true;
    }
}