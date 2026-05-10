package hosp.pharm.back.service;

import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.dto.update.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponseDto> getAll(final UserFilter filter, final Pageable pageable);

    UserResponseDto getById(final Long id);

    UserResponseDto create(final UserCreateDto dto);

    UserResponseDto update(final UserUpdateDto dto);

    void disable(final Long id);


}
