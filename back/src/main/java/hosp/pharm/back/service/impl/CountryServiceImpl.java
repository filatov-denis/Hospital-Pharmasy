package hosp.pharm.back.service.impl;

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
import java.util.NoSuchElementException;
import java.util.Optional;

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
        //add error handling

        final Optional<CountryEntity> optional = repository.findById(id);

        if(optional.isEmpty()) {
            //change
            throw new NoSuchElementException();
        }

        return mapper.toDto(optional.get());
    }
}
