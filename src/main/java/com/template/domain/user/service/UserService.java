package com.template.domain.user.service;

import com.template.domain.user.dto.UserCreateDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.dto.UserUpdateDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserDao;
import com.template.domain.user.repository.UserRepository;
import com.template.global.common.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.template.global.common.enums.CacheType.CacheNameConfig.USER_CACHE;
import static com.template.global.common.enums.CacheType.CacheNameConfig.USER_LIST_CACHE;
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


    @Transactional(readOnly = true)
    @Cacheable(value = USER_LIST_CACHE, keyGenerator = "customKeyGenerator")
    public List<UserDto.Response> findAllUserList() {
        List<UserEntity> entityList = userDao.findAll();

        return entityList.stream()
                .map(UserDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
//    @Cacheable(value = USER_CACHE, key = "#emailAddress")
    public UserDto.Response findUserByEmailAddress(String emailAddress) {
        return userDao.findUserByEmailAddressOptional(emailAddress).orElseThrow(
                () -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.toString())
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = USER_CACHE, key = "#userId")
    public UserDto.Response findUserById(Long userId) {
        UserEntity userEntity = userDao.findUserById(userId).orElseThrow(
                () -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.toString())
        );

        return UserDto.Response.from(userEntity);
    }

    @Caching(evict = {
            @CacheEvict(value = USER_CACHE, key = "#userUpdateDto.id"),
//            @CacheEvict(value = USER_CACHE, key = "#userUpdateDto.email"), // 한 유저가 이메일을 A에서 B로 변경할 경우, findUserByEmailAddress을 했을 때 캐시가 남아있어서 문제가 발생할 수 있음. 그렇기에 각주처리
            @CacheEvict(value = USER_LIST_CACHE, allEntries = true)
    })
    public UserUpdateDto.Response updateUser(UserUpdateDto.Request userUpdateDto) {
        UserEntity userEntity = userDao.findUserById(userUpdateDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.toString())
        );

        userEntity.update(userUpdateDto);

        return UserUpdateDto.Response.from(userEntity);
    }

    @CacheEvict(value = USER_LIST_CACHE, allEntries = true)
    public UserCreateDto.Response createNewUser(UserCreateDto.Request userDto) {
        if (Objects.isNull(userDto)) {
            throw new IllegalArgumentException(ERROR_WRONG_REQUEST.toString());
        }

        AuthorityEntity temporaryAuthorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);

        UserEntity userEntity = userDto.toEntityWithAuthority(temporaryAuthorityEntity);
        userEntity = userRepository.save(userEntity);

        return UserCreateDto.Response.from(userEntity.getId());
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
