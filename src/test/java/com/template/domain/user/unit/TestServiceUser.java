package com.template.domain.user.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.template.domain.user.AuthorityService;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserRepository;
import com.template.domain.user.UserService;
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
class TestServiceUser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AuthorityService authorityService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("save 테스트")
    void testSave_success() throws Exception {
        // given
        UserDto.Request userDto = objectMapper.readValue(getUserInfoJson(), UserDto.Request.class);
        AuthorityEntity authorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);
        UserEntity entity = userDto.toEntityWithAuthority(authorityEntity);

        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        // when
        UserEntity savedUserEntity = userService.save(entity);

        // then
        assertNotNull(savedUserEntity);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("findAll 테스트")
    void testFindAll_success() throws JsonProcessingException {
        // given
        UserDto.Request request1 = objectMapper.readValue(getUserInfoJson(), UserDto.Request.class);
        UserDto.Request request2= objectMapper.readValue(getUserInfoJson(), UserDto.Request.class);
        AuthorityEntity authorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);

        List<UserEntity> userEntityList = Arrays.asList(request1.toEntityWithAuthority(authorityEntity), request2.toEntityWithAuthority(authorityEntity));

        when(userRepository.findAll()).thenReturn(userEntityList);

        // when
        List<UserDto.Response> responseList = userService.findAllUserList();

        // then
        assertEquals(responseList.size(), userEntityList.size());
        verify(userRepository, times(1)).findAll();
        verify(userRepository, never()).save(request1.toEntityWithAuthority(authorityEntity));
    }

    private static String getUserInfoJson() {
        return
                "{"
                        + "\"id\":\"THIS_IS_USER_UUID_1\","
                        + "\"email\":\"user@example.com\","
                        + "\"phone\":\"012-3456-7890\","
                        + "\"name\":\"John Doe\","
                        + "\"password\":\"test123\""
                        + "}";
    }

}