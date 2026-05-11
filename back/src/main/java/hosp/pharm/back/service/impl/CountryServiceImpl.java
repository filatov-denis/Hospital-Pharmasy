package hosp.pharm.back.service.impl;

import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.filter.CountryFilter;
import hosp.pharm.back.mapper.CountryMapper;
import hosp.pharm.back.model.dto.response.CountryResponseDto;
import hosp.pharm.back.model.entity.CountryEntity;
import hosp.pharm.back.repository.CountryRepository;
import hosp.pharm.back.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryMapper mapper = CountryMapper.INSTANCE;

    private final CountryRepository repository;

    @Override
    public List<CountryResponseDto> getAll(CountryFilter filter) {
        final List<CountryEntity> countries = new ArrayList<>();

        if(StringUtils.isEmpty(filter.getName())) {
            countries.addAll(repository.findAll());
        } else {
            countries.addAll(repository.findByNameContains(filter.getName()));
        }

        return countries.stream().map(mapper::toDto).toList();
    }

    @Override
    public CountryResponseDto getById(Long id) {
        if(id == null) throw new NullIdentifierException();

        return mapper.toDto(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }
}
