package hosp.pharm.back.controller;

import hosp.pharm.back.filter.CountryFilter;
import hosp.pharm.back.model.dto.response.CountryResponseDto;
import hosp.pharm.back.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController(value = "/country")
@Tag(name = "Страны", description = "Содержит операции для работы со странами")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Получить список стран", description = "Выводит список стран по фильтру")
    public List<CountryResponseDto> getAll(@RequestBody final CountryFilter filter) {
        return countryService.getAll(filter);
    }

    @GetMapping("/id")
    @Operation(summary = "Получить конкретную страну", description = "Выводит информацию о конкретной стране")
    public CountryResponseDto getById(@RequestParam final Long id) {
        return countryService.getById(id);
    }

}
