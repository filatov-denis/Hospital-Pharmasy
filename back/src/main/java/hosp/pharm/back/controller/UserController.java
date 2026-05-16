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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Пользователи", description = "Содержит операции, связанные с пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователей", description = "Позволяет получить информацию о пользователях по нескольким параметрам")
    public Page<UserResponseDto> getAll(@Valid final UserFilter filter, final Pageable pageable) {
        return userService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Получить конкретного пользователя", description = "Выводит подробную информацию по конкретному пользователю")
    public UserResponseDto getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Добавление пользователя", description = "Позволяет добавить нового пользователя")
    public UserResponseDto create(@RequestBody @Valid final UserCreateDto dto) {
        return userService.create(dto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Обновление информации о пользователе", description = "Позволяет обновить информацию о пользователе")
    public UserResponseDto update(@RequestBody @Valid final UserUpdateDto dto) {
        return userService.update(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отключение пользователя", description = "Делает пользователя неактивным")
    public void disable(@PathVariable final Long id) {
        userService.disable(id);
    }

}
