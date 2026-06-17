package com.example.Abonement_demo_rest.graphql.exception;



import com.example.AbonementFitness.validation.NoActiveSubscriptionValidator;
import com.netflix.graphql.types.errors.TypedGraphQLError;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
;

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

    private static final Logger log = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters handlerParameters) {

        Throwable exception = handlerParameters.getException();


        log.error("GRAPHQL ERROR: {}", exception.getMessage(), exception);
        System.err.println("GRAPHQL EXCEPTION");
        System.err.println("Exception: " + exception.getClass().getName());
        System.err.println("Message: " + exception.getMessage());
        exception.printStackTrace();  // ← ПОЛНЫЙ STACKTRACE

        String message = exception.getMessage();
        TypedGraphQLError error;

        if (exception instanceof IllegalArgumentException) {
            error = TypedGraphQLError.newBadRequestBuilder()
                    .message(message != null ? message : "Некорректные входные данные")
                    .path(handlerParameters.getPath())
                    .build();
        } else {
            error = TypedGraphQLError.newInternalErrorBuilder()
                    .message("Внутренняя ошибка сервера: " + (message != null ? message : "неизвестная ошибка"))
                    .path(handlerParameters.getPath())
                    .build();
        }

        return CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult()
                        .error(error)
                        .build());
    }
}

