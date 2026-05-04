package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.StorageFullResponseDto;
import hosp.pharm.back.model.dto.response.StorageShortResponseDto;
import hosp.pharm.back.model.entity.StorageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {BatchMapper.class, ProductMapper.class})
public interface StorageMapper {

    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    StorageFullResponseDto toFullDto(final StorageEntity entity);

    StorageShortResponseDto toShortDto(final StorageEntity entity);

}
