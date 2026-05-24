package hosp.pharm.back.mapper;

import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.entity.RequestEntity;
import hosp.pharm.back.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(uses = {BatchMapper.class, ProductMapper.class})
public interface RequestMapper {

    String HANDLER_FORMATTED_NAME = "java(mapName(entity.getHandler()))";

    String CREATOR_FORMATTED_NAME = "java(mapName(entity.getCreator()))";

    String CREATION_DATE_FORMAT = "java(mapCreationDate(entity.getCreationDate()))";

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productName", source = "entity.requestBatch.targetBatch.product.name")
    @Mapping(target = "productCount", source = "entity.requestBatch.count")
    @Mapping(target = "creatorName", expression = CREATOR_FORMATTED_NAME)
    @Mapping(target = "handlerName", expression = HANDLER_FORMATTED_NAME)
    @Mapping(target = "creationDate", expression = CREATION_DATE_FORMAT)
    RequestShortResponseDto toShortDto(final RequestEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "creatorName", expression = CREATOR_FORMATTED_NAME)
    @Mapping(target = "handlerName", expression = HANDLER_FORMATTED_NAME)
    @Mapping(target = "creationDate", expression = CREATION_DATE_FORMAT)
    RequestFullResponseDto toFullDto(final RequestEntity entity);


    default String mapName(final UserEntity user) {
        if(user == null) return null;

        final StringBuilder builder = new StringBuilder();
        final List<String> names = new ArrayList<>();
        if(Objects.nonNull(user.getName())) names.add(user.getName());
        if(Objects.nonNull(user.getMiddlename())) names.add(user.getMiddlename());
        if(Objects.nonNull(user.getLastname())) names.add(user.getLastname());
        int counter = 0;

        if(names.isEmpty()) return null;

        for(final String name : names) {
            counter++;
            if(counter > 1) {
                builder.append(" ");
            }

            builder.append(name);
        }

        return builder.toString();
    }

    default String mapCreationDate(final LocalDateTime date) {
        if(date == null) return null;

        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
