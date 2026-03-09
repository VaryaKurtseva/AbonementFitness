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

/**
 * Контракт API для управления пользователями.
 * Реализующий контроллер в сервисе должен имплементировать этот интерфейс.
 */
@Tag(name = "Users", description = "Управление пользователями")
@RequestMapping(
        value = "/api/users",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public interface UsersApi {

    @Operation(
            summary = "Получить пользователя по ID",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Книга найдена")
    @ApiResponse(responseCode = "404", description = "Книга не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    EntityModel<UserResponse> getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id
    );

    @Operation(
            summary = "Список пользователя",
            description = """
                    Возвращает постраничный список пользователей с HATEOAS-ссылками.
                    Поддерживает комбинирование фильтров: userId,name и surname
                    можно передавать одновременно.
                    """,
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Постраничный список книг")
    @GetMapping
    PagedModel<EntityModel<UserResponse>> getAllUsers(
            @Parameter(description = "Фильтр по ID пользователя") @RequestParam(required = false) Long userId,
            @Parameter(description = "Фильтр по имени", example = "Варвара") @RequestParam(required = false) String name,
            @Parameter(description = "Фильтр по фамили", example = "Курцева") @RequestParam(required = false) String surname,
            @Parameter(description = "Номер страницы (0..N)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20") @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "Создать пользователя",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "201", description = "Пользователь создана. Location header содержит URI нового ресурса.")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь указанным userId не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь с таким ISBN уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest request);

    @Operation(
            summary = "Полное обновление пользователя (PUT)",
            description = "Заменяет все поля пользователя. "
                    + "Для обновления отдельных полей используйте PATCH.",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пользователь обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь с таким ISBN уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<UserResponse> updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    );

    @Operation(
            summary = "Частичное обновление пользователя (PATCH)",
            description = """
                    Обновляет только переданные поля (семантика JSON Merge Patch, RFC 7396).
                    Непереданные поля остаются без изменений. .
                    """,
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "200", description = "Пользователь обновлен")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Пользователь с таким ISBN уже существует",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<UserResponse> patchUser(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody PatchUserRequest request
    );

    @Operation(
            summary = "Удалить пользователя",
            security = @SecurityRequirement(name = AbonementFitnessApiContract.SECURITY_SCHEME_BEARER)
    )
    @ApiResponse(responseCode = "204", description = "Пользователь удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1") @PathVariable Long id
    );
}

