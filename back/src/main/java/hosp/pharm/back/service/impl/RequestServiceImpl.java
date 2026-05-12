package hosp.pharm.back.service.impl;

import hosp.pharm.back.dao.repository.RequestRepository;
import hosp.pharm.back.dao.selector.RequestQuerySelector;
import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.filter.AnalyticFilter;
import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.mapper.RequestMapper;
import hosp.pharm.back.model.dto.analytic.RequestAnalyticDto;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import hosp.pharm.back.model.entity.RequestEntity;
import hosp.pharm.back.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

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
        return null;
    }

    @Override
    public RequestFullResponseDto update(final RequestUpdateDto dto) {
        return null;
    }

    @Override
    public RequestAnalyticDto getAnalytic(AnalyticFilter filter) {
        return null;
    }

    private RequestEntity getRequestById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
