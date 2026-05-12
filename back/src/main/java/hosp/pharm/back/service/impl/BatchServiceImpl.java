package hosp.pharm.back.service.impl;

import hosp.pharm.back.dao.repository.BatchRepository;
import hosp.pharm.back.dao.selector.BatchQuerySelector;
import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.mapper.BatchMapper;
import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import hosp.pharm.back.model.entity.BatchEntity;
import hosp.pharm.back.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private BatchRepository batchRepository;

    private BatchQuerySelector querySelector;

    private final BatchMapper batchMapper = BatchMapper.INSTANCE;

    @Override
    public Page<BatchResponseDto> getAll(final Long storageId, final Pageable pageable) {
        final BatchFilter filter = new BatchFilter(storageId);
        final Page<BatchEntity> entities = querySelector.getByDynamicFilter(filter, pageable);
        final List<BatchResponseDto> dtos = entities.get().map(batchMapper::toDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    @Override
    public BatchResponseDto getById(final Long id) {
        return batchMapper.toDto(getBatchById(id));
    }

    @Override
    public BatchResponseDto create(final BatchCreateDto dto) {
        return null;
    }

    @Override
    public BatchResponseDto update(final BatchUpdateDto dto) {
        return null;
    }

    @Override
    public void disable(final Long id) {
        final BatchEntity entity = getBatchById(id);

        entity.setActive(false);
        batchRepository.save(entity);
    }

    private BatchEntity getBatchById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return batchRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
