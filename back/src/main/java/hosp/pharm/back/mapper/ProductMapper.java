package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.create.ProductCreateDto;
import hosp.pharm.back.model.dto.response.ProductResponseDto;
import hosp.pharm.back.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CountryMapper.class)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "countryId", source = "countryOfOrigin.id")
    @Mapping(target = "countryName", source = "countryOfOrigin.name")
    ProductResponseDto toDto(final ProductEntity entity);

    ProductEntity toEntity(final ProductCreateDto dto);

}
