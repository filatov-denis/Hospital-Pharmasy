package hosp.pharm.back.service.impl;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.constant.StatusType;
import hosp.pharm.back.dao.repository.BatchRepository;
import hosp.pharm.back.dao.repository.RequestBatchRepository;
import hosp.pharm.back.dao.repository.RequestRepository;
import hosp.pharm.back.dao.repository.StorageRepository;
import hosp.pharm.back.dao.selector.RequestQuerySelector;
import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.exception.UnavailableBatchException;
import hosp.pharm.back.exception.UnavailableStatusException;
import hosp.pharm.back.filter.AnalyticFilter;
import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.mapper.RequestMapper;
import hosp.pharm.back.model.dto.analytic.RequestAnalyticDto;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import hosp.pharm.back.model.entity.*;
import hosp.pharm.back.service.RequestService;
import hosp.pharm.back.service.UserService;
import hosp.pharm.back.state.Confirmed;
import hosp.pharm.back.state.Created;
import hosp.pharm.back.state.Delivering;
import hosp.pharm.back.state.RequestState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final BatchRepository batchRepository;

    private final StorageRepository storageRepository;

    private final RequestBatchRepository requestBatchRepository;

    private final UserService userService;

    private final RequestQuerySelector querySelector;

    private final RequestMapper requestMapper = RequestMapper.INSTANCE;

    @Override
    public Page<RequestShortResponseDto> getAll(final RequestFilter filter, final Pageable pageable) {
        final Page<RequestEntity> entities = querySelector.getByDynamicFilter(filter, pageable);
        final List<RequestShortResponseDto> dtos = entities.get().map(requestMapper::toShortDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    @Override
    public RequestFullResponseDto getById(final Long id) {
        return requestMapper.toFullDto(getRequestById(id));
    }

    @Override
    public RequestFullResponseDto create(final RequestCreateDto dto) {
        final UserEntity current = userService.getCurrentUser();

        final Long storageId = (current.getRoleName().equals(RoleName.NURSE.name()))
                ? current.getStorage().getId()
                : dto.getTargetStorageId();

        if (storageId == null) throw new NullIdentifierException();

        final BatchEntity sourceBatch = batchRepository.findById(dto.getSourceBatchId())
                .orElseThrow(EntityNotFoundException::new);
        final StorageEntity targetStorage = storageRepository.findById(current.getStorage().getId())
                .orElseThrow(EntityNotFoundException::new);

        if(!sourceBatch.isActive() || sourceBatch.getCount() - sourceBatch.getTotalReservedCount() < dto.getCount()) {
            throw new UnavailableBatchException();
        }

        final Optional<BatchEntity> optionalTarget = batchRepository.findById(dto.getTargetBatchId());

        final BatchEntity targetBatch = optionalTarget.orElse(new BatchEntity(
                sourceBatch.getProduct(),
                targetStorage,
                0,
                0,
                true,
                sourceBatch.getManufactureDate(),
                sourceBatch.getExpirationDate()));

        final RequestBatchEntity requestBatchEntity = new RequestBatchEntity();
        requestBatchEntity.setSourceBatch(sourceBatch);
        requestBatchEntity.setTargetBatch(targetBatch);
        requestBatchEntity.setCount(dto.getCount());

        final RequestEntity requestEntity = new RequestEntity(
                current,
                null,
                requestBatchEntity,
                StatusType.CREATED,
                null);

        final RequestEntity persisted = requestRepository.save(requestEntity);

        return requestMapper.toFullDto(persisted);
    }

    @Override
    public RequestFullResponseDto update(final RequestUpdateDto dto) {
        final RequestEntity entity = getRequestById(dto.getId());

        final RequestState currentState = switch(entity.getStatus()) {
            case StatusType.CREATED -> new Created(entity);
            case StatusType.CONFIRMED -> new Confirmed(entity);
            case StatusType.DELIVERING -> new Delivering(entity);
            default -> throw new UnavailableStatusException();
        };

        switch (dto.getStatus()){
            case StatusType.CONFIRMED -> currentState.toConfirmed();
            case StatusType.CANCELLED -> currentState.toCancelled();
            case StatusType.DELIVERING -> currentState.toDelivering();
            case StatusType.COMPLETED -> currentState.toCompleted();
            default -> throw new UnavailableStatusException();
        }

        final RequestEntity persisted = requestRepository.save(entity);

        return requestMapper.toFullDto(persisted);
    }

    @Override
    public RequestAnalyticDto getAnalytic(final AnalyticFilter filter) {
        return null;
    }

    private RequestEntity getRequestById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private static class RequestStatus {


    }

}
