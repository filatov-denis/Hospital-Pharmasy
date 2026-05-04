package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.response.RequestBatchResponseDto;
import hosp.pharm.back.model.entity.BatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ProductMapper.class)
public interface BatchMapper {

    BatchMapper INSTANCE = Mappers.getMapper(BatchMapper.class);

    BatchResponseDto toDto(final BatchEntity entity);

    RequestBatchResponseDto toRequestDto(final BatchEntity entity);

}
