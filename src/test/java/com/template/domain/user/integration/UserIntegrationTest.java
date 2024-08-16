package com.template.domain.user.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.template.domain.user.dto.UserCreateDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserDao;
import com.template.domain.user.repository.UserRepository;
import com.template.domain.user.service.AuthorityService;
import com.template.domain.user.service.UserService;
import com.template.global.common.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.template.global.common.enums.ErrorCode.ERROR_USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserIntegrationTest {
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PHONE = "012-3456-7890";
    private static final String TEST_NAME = "홍길동";
    private static final String TEST_PASSWORD = "test123";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserService userService;

    @SpyBean
    private UserDao userDao;

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private AuthorityService authorityService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createNewUser_test_1() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto()))
        );

        resultActions.andExpect(status().isCreated());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = objectMapper.readValue(content, new TypeReference<>() {
        });
        UserCreateDto.Response data = objectMapper.convertValue(resultMap.get("data"), new TypeReference<>() {
        });

        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertNotNull(data.getId());
    }

    @Test
    void findAllUserList_test_1() throws Exception {
        // given
        int numberOfRequests = 5;

        for (int i = 0; i < numberOfRequests; i++) {
            UserCreateDto.Request newUserDto = createNewUserDto(i);
            AuthorityEntity authorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);
            UserEntity entityWithAuthority = UserEntity.createInstance(newUserDto, authorityEntity);

            userService.save(entityWithAuthority);
        }

        // when
        ResultActions resultActions = mockMvc.perform(get("/user/list"));

        resultActions.andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = objectMapper.readValue(content, new TypeReference<>() {
        });
        List<UserDto.Response> data = objectMapper.convertValue(resultMap.get("data"), new TypeReference<>() {
        });

        // then
        verify(userRepository, times(numberOfRequests)).save(any(UserEntity.class));
        verify(userDao, times(1)).findAll();
        assertTrue(data.size() >= numberOfRequests);
    }

    @Test
    void findUserByEmailAddress_test_1() throws Exception {
        // given
        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto()))
        );

        // when
        String testEmail0 = TEST_EMAIL + 0;
        ResultActions resultActions = mockMvc.perform(
                get("/user/emailAddress")
                        .param("emailAddress", testEmail0)
        );

        resultActions.andExpect(status().isOk());
        String content = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, Object> resultMap = objectMapper.readValue(content, new TypeReference<>() {
        });
        UserDto.Response data = objectMapper.convertValue(resultMap.get("data"), new TypeReference<>() {
        });

        // then
        verify(userDao, times(1)).findUserByEmailAddressOptional(testEmail0);
        assertEquals(data.getEmail(), testEmail0);
    }

    @Test
    void findUserByEmailAddress_not_found_test_1() throws Exception {
        // given
        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto()))
        );

        String nonExistentEmail = "THIS_DOES_NOT_EXIST@example.com";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/emailAddress")
                        .param("emailAddress", nonExistentEmail)
        );

        resultActions.andExpect(status().is4xxClientError());
        Exception resolvedException = resultActions.andReturn().getResolvedException();

        // then
        verify(userDao, times(1)).findUserByEmailAddressOptional(nonExistentEmail);
        assertEquals(EntityNotFoundException.class, resolvedException.getClass());
        assertEquals(ERROR_USER_NOT_FOUND.toString(), resolvedException.getMessage());
    }

    private UserCreateDto.Request createNewUserDto() {
        return createNewUserDto(0);
    }

    private UserCreateDto.Request createNewUserDto(int index) {
        return UserCreateDto.Request.builder()
                .email(TEST_EMAIL + index)
                .phone(TEST_PHONE + index)
                .name(TEST_NAME + index)
                .password(TEST_PASSWORD + index)
                .build();
    }
}