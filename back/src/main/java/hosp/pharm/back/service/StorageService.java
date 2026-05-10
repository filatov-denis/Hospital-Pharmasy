package hosp.pharm.back.service;

import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.model.dto.create.StorageCreateDto;
import hosp.pharm.back.model.dto.response.StorageFullResponseDto;
import hosp.pharm.back.model.dto.response.StorageShortResponseDto;
import hosp.pharm.back.model.dto.update.StorageUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorageService {

    Page<StorageShortResponseDto> getAll(final StorageFilter filter, final Pageable pageable);

    StorageFullResponseDto getById(final Long id);

    StorageFullResponseDto create(final StorageCreateDto dto);

    StorageFullResponseDto update(final StorageUpdateDto dto);

    void disable(final Long id);

}
