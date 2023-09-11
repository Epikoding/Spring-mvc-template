package com.test.template.domain.test.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.template.domain.test.dto.TestDto;
import com.test.template.global.common.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TestControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("save 테스트")
    void addTest_success() throws Exception {
        // given
        String userInfoJson = getUserInfoJson();
        TestDto.Request request = objectMapper.readValue(userInfoJson, TestDto.Request.class);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInfoJson));

        // then
        resultActions.andExpect(status().isOk());
        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        TestDto.Response response = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        assertNotNull(response.getId());
        assertNotEquals(request.getId(), response.getId()); // request의 id와 response의 id가 달라야 함.
        try {
            UUID uuid = UUID.fromString(response.getId());
        } catch (IllegalArgumentException e) {
            fail("ID is not a valid UUID");
        }
    }

    @Test
    @DisplayName("findAll 테스트")
    void findAll_success() throws Exception {
        // given
        int numberOfRequests = 10;

        for (int i = 0; i < numberOfRequests; i++) {
            String userInfoJson = getUserInfoJson();

            ResultActions resultActions = mockMvc.perform(
                    post("/test")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userInfoJson));
        }

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/test/list"));

        resultActions.andExpect(status().isOk());
        Result result = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Result.class);
        List<TestDto.Response> responseList = objectMapper.convertValue(result.getData(), new TypeReference<>() {
        });

        // then
        assertEquals(responseList.size(), numberOfRequests);
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