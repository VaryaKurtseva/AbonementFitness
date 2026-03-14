package com.example.AbonementFitness.exception;

public class InvalidDateRange extends RuntimeException{
    public InvalidDateRange(long userId) {
        super("Неккоректный диапозон дат. Дата окончания должна быть позже даты начала");
    }
}
