package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.ProductResponseDto;
import hosp.pharm.back.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponseDto toDto(final ProductEntity entity);

}
