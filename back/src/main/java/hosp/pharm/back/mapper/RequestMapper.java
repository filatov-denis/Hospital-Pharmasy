package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.entity.RequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {BatchMapper.class, ProductMapper.class})
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestShortResponseDto toShortDto(final RequestEntity entity);

    RequestFullResponseDto toFullDto(final RequestEntity entity);

}
