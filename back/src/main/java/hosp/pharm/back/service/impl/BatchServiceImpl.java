package hosp.pharm.back.service.impl;

import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import hosp.pharm.back.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    @Override
    public Page<BatchResponseDto> getAll(Long storageId, Pageable pageable) {
        return null;
    }

    @Override
    public BatchResponseDto getById(Long id) {
        return null;
    }

    @Override
    public BatchResponseDto create(BatchCreateDto dto) {
        return null;
    }

    @Override
    public BatchResponseDto update(BatchUpdateDto dto) {
        return null;
    }

    @Override
    public void disable(Long id) {

    }
}
