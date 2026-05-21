package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.create.BatchCreateDto;
import hosp.pharm.back.model.dto.response.BatchResponseDto;
import hosp.pharm.back.model.dto.response.RequestBatchResponseDto;
import hosp.pharm.back.model.entity.BatchEntity;
import hosp.pharm.back.model.entity.RequestBatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ProductMapper.class)
public interface BatchMapper {

    BatchMapper INSTANCE = Mappers.getMapper(BatchMapper.class);

    BatchEntity toEntity(final BatchCreateDto dto);

    BatchResponseDto toDto(final BatchEntity entity);

    @Mapping(target = "manufactureDate", source = "entity.targetBatch.manufactureDate")
    @Mapping(target = "expirationDate", source = "entity.targetBatch.expirationDate")
    @Mapping(target = "sourceStorageName", source = "entity.sourceBatch.storage.name")
    @Mapping(target = "targetStorageName", source = "entity.targetBatch.storage.name")
    RequestBatchResponseDto toRequestDto(final RequestBatchEntity entity);


}
