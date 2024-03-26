package com.template.domain.user.service;

import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserDao;
import com.template.domain.user.repository.UserRepository;
import com.template.global.common.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.template.global.common.enums.CacheType.CacheTimeConfig.CACHE_10_SECOND;
import static com.template.global.common.enums.ErrorCode.ERROR_USER_NOT_FOUND;
import static com.template.global.common.enums.ErrorCode.ERROR_WRONG_REQUEST;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    // Repository
    private final UserRepository userRepository;
    private final UserDao userDao;

    // Service
    private final AuthorityService authorityService;

    @Cacheable(value = CACHE_10_SECOND, keyGenerator = "customKeyGenerator")
    public List<UserDto.Response> findAllUserList() {
        List<UserEntity> entity = userRepository.findAll();

        return entity.stream()
                .map(v -> UserDto.Response.from(v.getId()))
                .toList();
    }

    public UserDto.Response findUserByEmailAddress(String emailAddress) {
        return userDao.findUserByEmailAddressOptional(emailAddress).orElseThrow(
                () -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.toString())
        );
    }

    public CreateNewUserDto.Response createNewUser(CreateNewUserDto.Request userDto) {
        if (Objects.isNull(userDto)) {
            throw new IllegalArgumentException(ERROR_WRONG_REQUEST.toString());
        }

        AuthorityEntity temporaryAuthorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);

        UserEntity userEntity = userDto.toEntityWithAuthority(temporaryAuthorityEntity);
        userEntity = userRepository.save(userEntity);

        return CreateNewUserDto.Response.from(userEntity.getId());
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
