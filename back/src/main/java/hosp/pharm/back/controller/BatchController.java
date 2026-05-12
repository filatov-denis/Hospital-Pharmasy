package hosp.pharm.back.controller;

import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import hosp.pharm.back.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController(value = "/batch")
@Tag(name = "Партии", description = "Содержит операции, связанные с партиями")
public class BatchController {

    private final BatchService batchService;

    @GetMapping("/{storageId}")
    @Operation(summary = "Получить партии", description = "Позволяет получить партии конкретного склада")
    public Page<BatchResponseDto> getAllOfStorage(@PathVariable @NotNull final Long storageId, final Pageable pageable) {
        return batchService.getAll(storageId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить конкретного партию", description = "Выводит подробную информацию по конкретной партии")
    public BatchResponseDto getById(@PathVariable final Long id) {
        return batchService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Добавление партии товара", description = "Позволяет добавить нового партию товара")
    public BatchResponseDto create(@RequestBody @Valid final BatchCreateDto dto) {
        return batchService.create(dto);
    }

    @PutMapping
    @Operation(summary = "Обновление информации о партии", description = "Позволяет обновить информацию о партии")
    public BatchResponseDto update(@RequestBody @Valid final BatchUpdateDto dto) {
        return batchService.update(dto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Отключение партии", description = "Делает партию неактивной")
    public void disable(@PathVariable final Long id) {
        batchService.disable(id);
    }

}
