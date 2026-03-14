package com.example.AbonementFitness.validation;

import com.example.AbonementFitness.exception.ApiButtonExceptionUserId;
import com.example.AbonementFitness.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoActiveSubscriptionValidator implements ConstraintValidator<NoActiveSubscription, Long> {
    @Autowired
    private UserService userService;
    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        if (userId == null) throw new RuntimeException("Пользователь отсуствует");

        if(userService.hasActiveSubscription(userId)){
            throw new ApiButtonExceptionUserId(userId);
        }
        return true;
    }
}

