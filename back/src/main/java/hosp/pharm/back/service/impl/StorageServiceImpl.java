package hosp.pharm.back.service.impl;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NotAllowedForUserException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.filter.StorageFilter;
import hosp.pharm.back.mapper.StorageMapper;
import hosp.pharm.back.model.dto.create.StorageCreateDto;
import hosp.pharm.back.model.dto.response.StorageFullResponseDto;
import hosp.pharm.back.model.dto.response.StorageShortResponseDto;
import hosp.pharm.back.model.dto.update.StorageUpdateDto;
import hosp.pharm.back.model.entity.BatchEntity;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.dao.repository.StorageRepository;
import hosp.pharm.back.model.entity.UserEntity;
import hosp.pharm.back.service.StorageService;
import hosp.pharm.back.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    private final UserService userService;

    private final StorageMapper storageMapper = StorageMapper.INSTANCE;

    @Override
    public Page<StorageShortResponseDto> getAll(final StorageFilter filter, final Pageable pageable) {
        Page<StorageEntity> entities;

        if (StringUtils.isEmpty(filter.getName())) {
            entities = storageRepository.findByNameContainsAndActiveTrue(filter.getName(), pageable);
        } else {
            entities = storageRepository.findByActiveTrue(pageable);
        }

        final List<StorageShortResponseDto> dtos = entities.get().map(storageMapper::toShortDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    @Override
    public StorageFullResponseDto getById(final Long id) {
        final UserEntity authorized = userService.getCurrentUser();

        if(authorized.getRole().equals(RoleName.ROLE_NURSE) && !Objects.equals(authorized.getId(), id)) {
            throw new NotAllowedForUserException();
        }

        final StorageEntity entity = getStorageById(id);

        return storageMapper.toFullDto(entity);
    }

    @Override
    public StorageFullResponseDto create(final StorageCreateDto dto) {
        final StorageEntity entity = new StorageEntity();
        entity.setName(dto.getName());

        final StorageEntity persisted = storageRepository.save(entity);

        return storageMapper.toFullDto(persisted);
    }

    @Override
    public StorageFullResponseDto update(final StorageUpdateDto dto) {
        final StorageEntity entity = getStorageById(dto.getId());

        entity.setName(dto.getName());

        for(BatchEntity batch : entity.getBatches()) {
            if(entity.getActive() && !dto.getBatchIds().contains(entity.getId())) {
                batch.setActive(false);
            }
        }

        final StorageEntity persisted = storageRepository.save(entity);

        return storageMapper.toFullDto(persisted);
    }

    @Override
    @Transactional
    public void disable(final Long id) {
        final StorageEntity entity = getStorageById(id);

        entity.setActive(false);
        entity.getBatches().forEach(batch -> batch.setActive(false));

        storageRepository.save(entity);
    }

    private StorageEntity getStorageById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return storageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
