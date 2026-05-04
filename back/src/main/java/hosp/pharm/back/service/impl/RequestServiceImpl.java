package hosp.pharm.back.service.impl;

import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import hosp.pharm.back.repository.RequestRepository;
import hosp.pharm.back.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Page<RequestShortResponseDto> getAll(final RequestFilter filter, final Pageable pageable) {
        return null;
    }

    @Override
    public RequestFullResponseDto create(final RequestCreateDto dto) {
        return null;
    }

    @Override
    public RequestFullResponseDto update(final RequestUpdateDto dto) {
        return null;
    }
}
