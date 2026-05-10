package hosp.pharm.back.controller;

import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.dto.update.UserUpdateDto;
import hosp.pharm.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController(value = "/user")
@Tag(name = "Пользователи", description = "Содержит операции, связанные с пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить пользователей", description = "Позволяет получить информацию о пользователях по нескольким параметрам")
    public Page<UserResponseDto> getAll(@RequestBody @Valid final UserFilter filter, final Pageable pageable) {
        return userService.getAll(filter, pageable);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить конкретного пользователя", description = "Выводит подробную информацию по конкретному пользователю")
    public UserResponseDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Добавление пользователя", description = "Позволяет добавить нового пользователя")
    public UserResponseDto create(@RequestBody @Valid final UserCreateDto dto) {
        return userService.create(dto);
    }

    @PutMapping
    @Operation(summary = "Обновление информации о пользователе", description = "Позволяет обновить информацию о пользователе")
    public UserResponseDto update(@RequestBody @Valid final UserUpdateDto dto) {
        return userService.update(dto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Отключение пользователя", description = "Делает пользователя неактивным")
    public void disable(@PathVariable Long id) {
        userService.disable(id);
    }

}
