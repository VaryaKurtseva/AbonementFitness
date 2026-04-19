package com.example.Abonement_demo_rest.graphql.exception;



import com.example.AbonementFitness.validation.NoActiveSubscriptionValidator;
import com.netflix.graphql.types.errors.TypedGraphQLError;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Обработчик исключений для GraphQL DataFetcher'ов.
 *
 * В REST API исключения преобразуются в HTTP-статусы (404, 409, 400).
 * В GraphQL нет HTTP-статусов для ошибок — все ответы приходят с HTTP 200,
 * а ошибки помещаются в массив "errors" в теле ответа.
 *
 * Этот обработчик перехватывает доменные исключения и преобразует их
 * в типизированные GraphQL-ошибки с понятными сообщениями и классификацией.
 *
 * DGS автоматически обнаруживает реализацию DataFetcherExceptionHandler
 * как Spring-компонент и подставляет её вместо обработчика по умолчанию.
 */
@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters handlerParameters) {

        Throwable exception = handlerParameters.getException();
        String message = exception.getMessage();
        TypedGraphQLError error;




        if (exception instanceof IllegalArgumentException) {
            error = TypedGraphQLError.newBadRequestBuilder()
                    .message(message != null ? message : "Некорректные входные данные")
                    .path(handlerParameters.getPath())
                    .build();
        }
        else if (message != null && message.contains("активный абонемент")) {
            error = TypedGraphQLError.newConflictBuilder()
                    .message(message)
                    .path(handlerParameters.getPath())
                    .build();
        }



        // Все остальные исключения — внутренняя ошибка сервера.
        // Не раскрываем детали клиенту в целях безопасности.
        var error1 = TypedGraphQLError.newInternalErrorBuilder()
                .message("Внутренняя ошибка сервера")
                .path(handlerParameters.getPath())
                .build();

        return CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult()
                        .error(error1)
                        .build());
    }
}
