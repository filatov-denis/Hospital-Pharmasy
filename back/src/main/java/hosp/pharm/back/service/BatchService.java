package hosp.pharm.back.service;

import hosp.pharm.back.filter.BatchFilter;
import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.update.BatchUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BatchService {

    Page<BatchResponseDto> getAll(final BatchFilter filter, final Pageable pageable);

    List<BatchResponseDto> getAllOfMainStorage();

    BatchResponseDto getById(final Long id);

    BatchResponseDto create(final BatchCreateDto dto);

    BatchResponseDto update(final BatchUpdateDto dto);

    void disable(final Long id);

}
