package com.example.AbonementFitness.exception;

public class ApiButtonExceptionUserId extends RuntimeException {
    public ApiButtonExceptionUserId(long userId) {
        super("У пользователя с id =" + String.valueOf(userId) + " есть активный абонемент");
    }
}
