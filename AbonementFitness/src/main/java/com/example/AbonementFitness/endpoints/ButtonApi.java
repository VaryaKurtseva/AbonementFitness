package com.example.AbonementFitness.endpoints;


import com.example.AbonementFitness.dto.*;
import com.example.AbonementFitness.config.AbonementFitnessApiContract;
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
public interface ButtonApi {
    @Operation(
            summary = "Список кнопок",
            description = """
                    Возвращает постраничный список пользователей с HATEOAS-ссылками.
                    Поддерживает комбинирование фильтров: userId,name и surname
                    можно передавать одновременно.
                    """,
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список пользователей")
    @GetMapping
    PagedModel<EntityModel<ButtonResponse>> getAllButtons(
            @Parameter(description = "Фильтр по ID user") @RequestParam(required = false) Long userId,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );



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
    @ApiResponse(responseCode = "403", description = "Отказано в продлении (низкая репутация/нарушения)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(value = "/renew",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<ButtonResponse>> renewSubscription(@Valid @RequestBody ButtonRequest request);
    @Operation(
            summary = "Ответ от системы, после того, как пользователь нажал на кнопку",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Пользователь удачно продлил абонемент")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь с указанным Именем и Фамилией не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{requestId}")
    ResponseEntity<EntityModel<ButtonResponse>>  buttonRenewResponse(
            @Parameter(description = "ID запроса", required = true, example = "1")
            @PathVariable Long requestId);

    @Operation(
            summary = "Полное обновление кнопки (PUT)",
            description = "Заменяет все поля кнопки. "
                    + "Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Кнопка обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Кнопка не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ButtonResponse> updateButton(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody ButtonRequest request
    );

    @Operation(
            summary = "Частичное обновление пользователя (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений. .
                    """,
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Кнопка обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Кнопка не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<ButtonResponse> patchButton(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchButtonRequest request
    );
    @Operation(
            summary = "Удалить кнопку",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Кнопка удалена")
    @ApiResponse(responseCode = "404", description = "Кнопка не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteButton(
            @Parameter(description = "ID кнопки", required = true, example = "1") @PathVariable Long id
    );
}
