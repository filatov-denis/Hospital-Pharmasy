package hosp.pharm.back.service;

import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.dto.analytic.RequestAnalyticDto;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Page<RequestShortResponseDto> getAll(final RequestFilter filter, final Pageable pageable);

    RequestFullResponseDto getById(final Long id);

    RequestFullResponseDto create(final RequestCreateDto dto);

    RequestFullResponseDto update(final RequestUpdateDto dto);

    RequestAnalyticDto getAnalytic();

}
