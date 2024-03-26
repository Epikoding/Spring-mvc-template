package com.template.domain.user.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.template.domain.user.service.AuthorityService;
import com.template.domain.user.service.UserService;
import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserDao;
import com.template.domain.user.repository.UserRepository;
import com.template.global.common.Result;
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
@ActiveProfiles("local") // local, dev, prd. local 이외의 환경에서는 RateLimitingFilter가 적용됨. FilterConfig @Profile("!local") 참고
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserIntegrationTest {
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
        // given


        // when
        ResultActions resultActions = mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto("user@example.com", "012-3456-7890", "John Doe", "test123")))
        );


        resultActions.andExpect(status().isOk());

        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        UserDto.Response response = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));

        assertNotNull(response.getId());
    }

    @Test
    void findAllUserList_test_1() throws Exception {
        // given
        int numberOfRequests = 5;

        for (int i = 0; i < numberOfRequests; i++) {
            CreateNewUserDto.Request newUserDto = createNewUserDto("user@example.com" + i, "012-3456-7890" + i, "John Doe" + i, "test123" + i);

            AuthorityEntity authorityEntity = authorityService.getAuthorityEntityByRole(Role.TEMPORARY);
            UserEntity entityWithAuthority = newUserDto.toEntityWithAuthority(authorityEntity);

            userService.save(entityWithAuthority);
        }

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/list"));

        resultActions.andExpect(status().isOk());
        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        List<UserDto.Response> responseList = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        // then
        verify(userRepository, times(1)).findAll();

        assertTrue(responseList.size() >= numberOfRequests);
    }

    @Test
    void findUserByEmailAddress_test_1() throws Exception {
        // given
        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto("user@example.com", "012-3456-7890", "John Doe", "test123")))
        );

        String emailAddress = "user@example.com";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/emailAddress")
                .param("emailAddress", emailAddress)
        );

        resultActions.andExpect(status().isOk());
        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        UserDto.Response response = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        // then
        verify(userDao, times(1)).findUserByEmailAddressOptional(emailAddress);

        assertEquals(response.getEmail(), emailAddress);
    }

    @Test
    void findUserByEmailAddress_not_found_test_1() throws Exception {
        // given
        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto("user@example.com", "012-3456-7890", "John Doe", "test123")))
        );

        String emailAddress = "THIS_DOES_NOT_EXIST@example.com";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/emailAddress")
                        .param("emailAddress", emailAddress)
        );

        resultActions.andExpect(status().is4xxClientError());
        Exception resolvedException = resultActions.andReturn().getResolvedException();

        // then
        verify(userDao, times(1)).findUserByEmailAddressOptional(emailAddress);

        assertEquals(EntityNotFoundException.class, resolvedException.getClass());
        assertEquals(ERROR_USER_NOT_FOUND.toString(), resolvedException.getMessage());
    }


    private CreateNewUserDto.Request createNewUserDto(final String email, final String phone, final String name, final String password) {
        return CreateNewUserDto.Request.builder()
                .email(email)
                .phone(phone)
                .name(name)
                .password(password)
                .build();
    }

}