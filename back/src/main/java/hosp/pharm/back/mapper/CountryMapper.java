package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.CountryResponseDto;
import hosp.pharm.back.model.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    CountryResponseDto toDto(CountryEntity entity);

}
