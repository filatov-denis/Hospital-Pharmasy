package hosp.pharm.back.service.impl;

import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.model.dto.create.StorageCreateDto;
import hosp.pharm.back.model.dto.response.StorageFullResponseDto;
import hosp.pharm.back.model.dto.response.StorageShortResponseDto;
import hosp.pharm.back.model.dto.update.StorageUpdateDto;
import hosp.pharm.back.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Override
    public Page<StorageShortResponseDto> getAll(StorageFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public StorageFullResponseDto getById(Long id) {
        return null;
    }

    @Override
    public StorageFullResponseDto create(StorageCreateDto dto) {
        return null;
    }

    @Override
    public StorageFullResponseDto update(StorageUpdateDto dto) {
        return null;
    }

    @Override
    public void disable(Long id) {

    }
}
