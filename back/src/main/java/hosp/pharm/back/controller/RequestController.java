package hosp.pharm.back.controller;

import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.dto.analytic.RequestAnalyticDto;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import hosp.pharm.back.service.RequestService;
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
@RequestMapping("/request")
@Tag(name = "Запросы", description = "Содержит операции, связанные с запросами партий")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Получить запросы", description = "Позволяет получить список запросов по параметрам")
    public Page<RequestShortResponseDto> getAll(@Valid final RequestFilter filter, final Pageable pageable) {
        return requestService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'NURSE')")
    @Operation(summary = "Получить конкретный запрос", description = "Выводит подробную информацию по конкретному запросу")
    public RequestFullResponseDto getById(@PathVariable final Long id) {
        return requestService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    @Operation(summary = "Добавление запроса", description = "Позволяет добавить новый запрос")
    public RequestFullResponseDto create(@Valid @RequestBody final RequestCreateDto dto) {
        return requestService.create(dto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    @Operation(summary = "Обновление информации запроса", description = "Позволяет обновить состояние запроса")
    public RequestFullResponseDto update(@Valid @RequestBody final RequestUpdateDto dto) {
        return requestService.update(dto);
    }


    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    @Operation(summary = "Аналитика запросов", description = "Выводит аналитику запросов за месяц")
    public RequestAnalyticDto analytic() {
        return requestService.getAnalytic();
    }

}
