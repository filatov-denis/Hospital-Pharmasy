package hosp.pharm.back.service;

import hosp.pharm.back.filter.ProductFilter;
import hosp.pharm.back.model.dto.create.ProductCreateDto;
import hosp.pharm.back.model.dto.response.ProductResponseDto;
import hosp.pharm.back.model.dto.update.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponseDto> getAll(final ProductFilter filter, final Pageable pageable);

    ProductResponseDto getById(final Long id);

    ProductResponseDto create(final ProductCreateDto dto);

    ProductResponseDto update(final ProductUpdateDto dto);

    void disable(final Long id);

}
