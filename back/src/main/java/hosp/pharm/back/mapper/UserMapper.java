package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Mapper
public interface UserMapper {

    String LINKED_STORAGE_ID = "java(mapStorageId(entity.getStorage()))";

    String LINKED_STORAGE_NAME = "java(mapStorageName(entity.getStorage()))";

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "linkedStorageName", expression = LINKED_STORAGE_NAME)
    @Mapping(target = "linkedStorageId", expression = LINKED_STORAGE_ID)
    UserResponseDto toDto(final UserEntity entity);

    UserEntity toEntity(final UserCreateDto dto);

    default UserDetails toAuthUser(final UserEntity entity) {
        final GrantedAuthority role = new SimpleGrantedAuthority(entity.getRole().name());

        return new User(entity.getId().toString(), null, List.of(role));
    }

    default Long mapStorageId(final StorageEntity entity) {
        if(entity == null) return null;

        return entity.getId();
    }

    default String mapStorageName(final StorageEntity entity) {
        if(entity == null) return null;

        return entity.getName();
    }


}
