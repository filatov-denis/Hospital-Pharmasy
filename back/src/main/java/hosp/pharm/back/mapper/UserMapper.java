package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toDto(final UserEntity entity);

}
