package com.example.AbonementFitness.endpoints;

import com.example.AbonementFitness.config.AbonementFitnessApiContract;
import com.example.AbonementFitness.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "ButtonRenew", description = "Управление продлениями абонементов")
@RequestMapping(
        value = "/api/button",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface ButtonRenewApi {



    @Operation(
            summary = "Нажать на кнопку",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Пользователь удачно продлил абонемент")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь с указанным Именем и Фамилией не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь с таким ISBN уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserResponse>>  renewSubscription(@Valid @RequestBody ButtonRenewSubscriptionRequest request);



    @Operation(
            summary = "Удалить кнопку",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Кнопка удалена")
    @ApiResponse(responseCode = "404", description = "Кнопка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteButton(
            @Parameter(description = "ID кнопки", required = true, example = "1") @PathVariable Long id
    );
}
