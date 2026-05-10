package hosp.pharm.back.controller;

import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.model.dto.create.StorageCreateDto;
import hosp.pharm.back.model.dto.response.StorageFullResponseDto;
import hosp.pharm.back.model.dto.response.StorageShortResponseDto;
import hosp.pharm.back.model.dto.update.StorageUpdateDto;
import hosp.pharm.back.service.StorageService;
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
@RestController(value = "/storage")
@Tag(name = "Склады", description = "Содержит операции, связанные с настройкой складов")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    @Operation(summary = "Получить склады", description = "Позволяет получить список складов")
    public Page<StorageShortResponseDto> getAll(@RequestBody @Valid final StorageFilter filter, final Pageable pageable) {
        return storageService.getAll(filter, pageable);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить конкретный склад", description = "Выводит подробную информацию по конкретному складу")
    public StorageFullResponseDto getById(@PathVariable final Long id) {
        return storageService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Добавление склада", description = "Позволяет добавить новый склад")
    public StorageFullResponseDto create(@RequestBody @Valid final StorageCreateDto dto) {
        return storageService.create(dto);
    }

    @PutMapping
    @Operation(summary = "Обновление информации склада", description = "Позволяет обновить информацию склада")
    public StorageFullResponseDto update(@RequestBody @Valid final StorageUpdateDto dto) {
        return storageService.update(dto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Отключение склада", description = "Делает склад неактивным")
    public void disable(@PathVariable Long id) {
        storageService.disable(id);
    }

}
