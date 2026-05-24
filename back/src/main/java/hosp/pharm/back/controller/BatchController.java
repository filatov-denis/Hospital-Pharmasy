package hosp.pharm.back.controller;

import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import hosp.pharm.back.service.BatchService;
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
@RequestMapping("/batch")

@Tag(name = "Партии", description = "Содержит операции, связанные с партиями")
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Получить партии", description = "Позволяет получить партии конкретного склада")
    public Page<BatchResponseDto> getAllOfStorage(@Valid final BatchFilter filter, final Pageable pageable) {
        return batchService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Получить конкретного партию", description = "Выводит подробную информацию по конкретной партии")
    public BatchResponseDto getById(@PathVariable final Long id) {
        return batchService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    @Operation(summary = "Добавление партии товара", description = "Позволяет добавить нового партию товара")
    public BatchResponseDto create(@RequestBody @Valid final BatchCreateDto dto) {
        return batchService.create(dto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Обновление информации о партии", description = "Позволяет обновить информацию о партии")
    public BatchResponseDto update(@RequestBody @Valid final BatchUpdateDto dto) {
        return batchService.update(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Отключение партии", description = "Делает партию неактивной")
    public void disable(@PathVariable final Long id) {
        batchService.disable(id);
    }

}
