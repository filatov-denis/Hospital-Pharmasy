package hosp.pharm.back.service.impl;

import hosp.pharm.back.dao.repository.BatchRepository;
import hosp.pharm.back.dao.repository.ProductRepository;
import hosp.pharm.back.dao.repository.StorageRepository;
import hosp.pharm.back.dao.selector.BatchQuerySelector;
import hosp.pharm.back.exception.*;
import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.mapper.BatchMapper;
import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import hosp.pharm.back.model.entity.BatchEntity;
import hosp.pharm.back.model.entity.ProductEntity;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    private final StorageRepository storageRepository;

    private final ProductRepository productRepository;

    private final BatchQuerySelector querySelector;

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
        final BatchEntity batch = batchMapper.toEntity(dto);

        if(dto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BatchAlreadyExpiredException();
        }

        final ProductEntity product = productRepository.findByIdAndActiveTrue(dto.getProductId())
                .orElseThrow(EntityNotFoundException::new);
        final StorageEntity storage = storageRepository.findByIdAndActiveTrue(1L)
                .orElseThrow(EntityNotFoundException::new);

        batch.setProduct(product);
        batch.setStorage(storage);

        final BatchEntity persisted = batchRepository.save(batch);
        return batchMapper.toDto(persisted);
    }

    @Override
    public BatchResponseDto update(final BatchUpdateDto dto) {
        final BatchEntity batch = getBatchById(dto.getId());

        if(dto.getManufactureDate() != null) batch.setManufactureDate(dto.getManufactureDate());

        if(dto.getCount() != null) {
            if(dto.getCount() < 0 || batch.getTotalReservedCount() < dto.getCount()) {
                throw new NotEnoughProductException();
            }
            batch.setCount(dto.getCount());
        }

        if(dto.getExpirationDate() != null) {
            if(dto.getExpirationDate().isBefore(LocalDate.now())) {
                throw new BatchAlreadyExpiredException();
            }
            batch.setExpirationDate(dto.getExpirationDate());
        }

        final BatchEntity persisted = batchRepository.save(batch);
        return batchMapper.toDto(persisted);
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
