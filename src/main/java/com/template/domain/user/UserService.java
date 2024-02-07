package com.template.domain.user;

import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserRepository;
import com.template.global.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.template.global.common.enums.CacheType.CacheTimeConfig.CACHE_10_SECOND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final AuthorityService authorityService;

    @Cacheable(value = CACHE_10_SECOND, keyGenerator = "customKeyGenerator")
    public List<UserDto.Response> findAllUserList() {
        List<UserEntity> entity = userRepository.findAll();

        return entity.stream()
                .map(v -> UserDto.Response.from(v.getId()))
                .toList();
    }


    public CreateNewUserDto.Response createNewUser(CreateNewUserDto.Request userDto) {
        if (Objects.isNull(userDto)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
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
