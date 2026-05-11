package hosp.pharm.back.service;

import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.dto.update.UserUpdateDto;
import hosp.pharm.back.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService extends UserDetailsService {

    PasswordEncoder getEncoder();

    Page<UserResponseDto> getAll(final UserFilter filter, final Pageable pageable);

    UserResponseDto getById(final Long id);

    UserResponseDto create(final UserCreateDto dto);

    UserResponseDto update(final UserUpdateDto dto);

    void disable(final Long id);

    UserEntity getUserById(final Long id);

    UserEntity getCurrentUser();
}
