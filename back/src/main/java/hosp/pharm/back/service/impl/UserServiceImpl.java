package hosp.pharm.back.service.impl;

import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.dto.update.UserUpdateDto;
import hosp.pharm.back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable) {
        return null;
    }

    @Override
    public UserResponseDto getById(Long id) {
        return null;
    }

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        return null;
    }

    @Override
    public UserResponseDto update(UserUpdateDto dto) {
        return null;
    }

    @Override
    public void disable(Long id) {

    }
}
