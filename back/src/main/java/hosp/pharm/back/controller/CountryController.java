package hosp.pharm.back.controller;

import hosp.pharm.back.filter.CountryFilter;
import hosp.pharm.back.model.dto.response.CountryResponseDto;
import hosp.pharm.back.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
@Tag(name = "Страны", description = "Содержит операции для работы со странами")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Получить список стран", description = "Выводит список стран по фильтру")
    public List<CountryResponseDto> getAll(@Valid CountryFilter filter) {
        return countryService.getAll(filter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить конкретную страну", description = "Выводит информацию о конкретной стране")
    public CountryResponseDto getById(@PathVariable final Long id) {
        return countryService.getById(id);
    }

}
