package hosp.pharm.back.service;

import hosp.pharm.back.filter.CountryFilter;
import hosp.pharm.back.model.dto.response.CountryResponseDto;

import java.util.List;

public interface CountryService {

    List<CountryResponseDto> getAll(final CountryFilter filter);

    CountryResponseDto getById(final Long id);

}
