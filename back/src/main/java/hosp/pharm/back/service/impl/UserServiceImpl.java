package hosp.pharm.back.service.impl;

import hosp.pharm.back.constant.RoleName;
import hosp.pharm.back.dao.repository.StorageRepository;
import hosp.pharm.back.dao.repository.UserRepository;
import hosp.pharm.back.dao.selector.UserQuerySelector;
import hosp.pharm.back.exception.*;
import hosp.pharm.back.filter.UserFilter;
import hosp.pharm.back.mapper.UserMapper;
import hosp.pharm.back.model.dto.create.UserCreateDto;
import hosp.pharm.back.model.dto.response.UserResponseDto;
import hosp.pharm.back.model.dto.update.UserUpdateDto;
import hosp.pharm.back.model.entity.StorageEntity;
import hosp.pharm.back.model.entity.UserEntity;
import hosp.pharm.back.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserQuerySelector querySelector;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final StorageRepository storageRepository;

    @Override
    public PasswordEncoder getEncoder() {
        return encoder;
    }

    @Override
    public Page<UserResponseDto> getAll(final UserFilter filter, final Pageable pageable) {
        final Page<UserEntity> entities = querySelector.getByDynamicFilter(filter, pageable);
        final List<UserResponseDto> dtos = entities.get().map(userMapper::toDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    @Override
    public UserResponseDto getById(final Long id) {
        validateUserOperations(id);

        return userMapper.toDto(getUserById(id));
    }

    @Override
    @Transactional
    public UserResponseDto create(final UserCreateDto dto) {
        final UserEntity entity = userMapper.toEntity(dto);

        if (userRepository.findByUsernameAndActiveTrue(entity.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        if(dto.getRole().equals(RoleName.ROLE_ADMIN)) {
            throw new NotAllowedRoleException();
        }

        setStorage(entity, dto.getLinkedStorageId());
        entity.setPassword(encoder.encode(dto.getPassword()));

        final UserEntity persisted = userRepository.save(entity);

        return userMapper.toDto(persisted);
    }

    @Override
    @Transactional
    public UserResponseDto update(final UserUpdateDto dto) {
        validateUserOperations(dto.getId());

        final UserEntity entity = getUserById(dto.getId());

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setLastname(dto.getLastname());
        setStorage(entity, dto.getLinkedStorageId());

        final UserEntity persisted = userRepository.save(entity);

        return userMapper.toDto(persisted);
    }

    @Override
    public void disable(final Long id) {
        final UserEntity entity = getUserById(id);

        entity.setActive(false);
        userRepository.save(entity);
    }

    @Override
    public UserEntity getCurrentUser() {
        final Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final Optional<UserEntity> optional = userRepository.findById(Long.parseLong(username));
        if (optional.isEmpty()) throw new UsernameNotFoundException(username);

        return userMapper.toAuthUser(optional.get());
    }

    @Override
    public UserEntity getUserById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private void setStorage(final UserEntity entity, final Long storageId) {
        if(storageId != null) {
            final Optional<StorageEntity> optional = storageRepository.findById(storageId);

            if (optional.isEmpty()) throw new EntityNotFoundException();

            final StorageEntity storage = optional.get();
            storage.getUsers().add(entity);
            entity.setStorage(storage);
        }
    }

    private void validateUserOperations(final Long requestedUserId) {
        final UserEntity authorized = getCurrentUser();

        if(!authorized.getRole().equals(RoleName.ROLE_ADMIN) && !Objects.equals(authorized.getId(), requestedUserId)) {
            throw new NotAllowedForUserException();
        }
    }

}
