package hosp.pharm.back.controller;

import hosp.pharm.back.filter.ProductFilter;
import hosp.pharm.back.model.dto.create.ProductCreateDto;
import hosp.pharm.back.model.dto.response.ProductResponseDto;
import hosp.pharm.back.model.dto.update.ProductUpdateDto;
import hosp.pharm.back.service.ProductService;
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
@RequestMapping("/product")
@Tag(name = "Продукты", description = "Содержит операции для работы с продуктами")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('NURSE', 'ADMIN', 'PHARMACIST')")
    @Operation(summary = "Получить продукты", description = "Позволяет получить список продуктов")
    public Page<ProductResponseDto> getAll(@Valid final ProductFilter filter, final Pageable pageable) {
        return productService.getAll(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('NURSE', 'ADMIN', 'PHARMACIST')")
    @Operation(summary = "Получить конкретный продукт", description = "Выводит подробную информацию по конкретному продукту")
    public ProductResponseDto getById(@PathVariable final Long id) {
        return productService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Добавление продукта", description = "Позволяет добавить новый продукт")
    public ProductResponseDto create(@RequestBody @Valid final ProductCreateDto dto) {
        return productService.create(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление информации продукта", description = "Позволяет обновить информацию продукта")
    public ProductResponseDto update(@RequestBody @Valid final ProductUpdateDto dto) {
        return productService.update(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отключение продукта", description = "Делает продукт неактивным")
    public void disable(@PathVariable final Long id) {
        productService.disable(id);
    }


}
