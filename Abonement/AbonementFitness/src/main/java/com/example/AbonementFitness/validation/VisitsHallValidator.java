package com.example.AbonementFitness.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Проверяет, что посещения зала является корректным между 1 и 220.

 * Алгоритмическая проверка контрольной цифры (алгоритм Луна) намеренно не выполняется:
 * валидатор должен быть быстрым, а точность важна на уровне интеграционных тестов.
 */

public class VisitsHallValidator implements ConstraintValidator<ValidVisitsHall, Integer> {
    @Override
    public boolean isValid(Integer visitsHall, ConstraintValidatorContext constraintValidatorContext) {
        if (visitsHall == null) return true;

        if(visitsHall < 0) return false;

        if (visitsHall >= 1 && visitsHall <= 220) return true;
        else return false;
    }
}
