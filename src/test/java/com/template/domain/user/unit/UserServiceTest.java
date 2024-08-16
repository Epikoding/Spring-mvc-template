package com.template.domain.user.unit;

import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.AuthorityDao;
import com.template.domain.user.repository.UserDao;
import com.template.domain.user.repository.UserRepository;
import com.template.domain.user.service.AuthorityService;
import com.template.domain.user.service.UserService;
import com.template.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityDao authorityDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AuthorityService authorityService;

    private UserDto.Request userDto;
    private AuthorityEntity authorityEntity;

    @BeforeEach
    void init() {
        setupTestData();
    }

    private void setupTestData() {
        userDto = UserDto.Request.builder()
                .uuid("THIS_IS_USER_UUID_1")
                .email("user@example.com")
                .phone("012-3456-7890")
                .name("홍길동")
                .password("test123")
                .build();

        authorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);
    }

    @Test
    @DisplayName("save 테스트")
    void testSave_success() {
        // given
        UserEntity entity = UserEntity.createInstance(userDto, authorityEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        // when
        UserEntity savedUserEntity = userService.save(entity);

        // then
        assertNotNull(savedUserEntity);
        assertEquals(entity.getEmail(), savedUserEntity.getEmail());
        assertEquals(entity.getPhone(), savedUserEntity.getPhone());
        assertEquals(entity.getName(), savedUserEntity.getName());
        assertEquals(entity.getPassword(), savedUserEntity.getPassword());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("findAll 테스트")
    void testFindAll_success() {
        // given
        List<UserEntity> userEntityList = Arrays.asList(
                UserEntity.createInstance(userDto, authorityEntity),
                UserEntity.createInstance(userDto, authorityEntity)
        );

        when(userDao.findAll()).thenReturn(userEntityList);

        // when
        List<UserDto.Response> responseList = userService.findAllUserList();

        // then
        assertEquals(userEntityList.size(), responseList.size());
        verify(userDao, times(1)).findAll();
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}