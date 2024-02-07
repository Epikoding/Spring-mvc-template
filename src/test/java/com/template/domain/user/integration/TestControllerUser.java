package com.template.domain.user.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.template.domain.user.AuthorityService;
import com.template.domain.user.UserService;
import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.dto.UserDto;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.domain.user.repository.UserRepository;
import com.template.global.common.Result;
import com.template.global.common.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TestControllerUser {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("save 테스트")
    void createNewUser_test_success_1() throws Exception {
        // given


        // when
        ResultActions resultActions = mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createNewUserDto("user@example.com", "012-3456-7890", "John Doe", "test123")))
        );

        // then
        resultActions.andExpect(status().isOk());
        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        UserDto.Response response = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        assertNotNull(response.getId());
    }

    @Test
    @DisplayName("findAll 테스트")
    void findAll_success() throws Exception {
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
        assertTrue(responseList.size() >= numberOfRequests);
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