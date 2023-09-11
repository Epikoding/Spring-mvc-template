package com.test.template.domain.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.template.domain.test.dto.TestDto;
import com.test.template.domain.test.entity.TestEntity;
import com.test.template.domain.test.repository.TestRepository;
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
class TestServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("save 테스트")
    void testSave_success() throws Exception {
        // given
        TestDto.Request request = objectMapper.readValue(getUserInfoJson(), TestDto.Request.class);
        TestEntity entity = request.toEntity();

        when(testRepository.save(any(TestEntity.class))).thenReturn(entity);
        
        // when
        TestDto.Response actualResponse = testService.save(request);

        // then
        assertNotNull(actualResponse);
        verify(testRepository, times(1)).save(any(TestEntity.class));
    }

    @Test
    @DisplayName("findAll 테스트")
    void testFindAll_success() throws JsonProcessingException {
        // given
        TestDto.Request request1 = objectMapper.readValue(getUserInfoJson(), TestDto.Request.class);
        TestDto.Request request2= objectMapper.readValue(getUserInfoJson(), TestDto.Request.class);

        List<TestEntity> testEntityList = Arrays.asList(request1.toEntity(), request2.toEntity());

        when(testRepository.findAll()).thenReturn(testEntityList);

        // when
        List<TestDto.Response> responseList = testService.findAll();

        // then
        assertEquals(responseList.size(), testEntityList.size());
        verify(testRepository, times(1)).findAll();
        verify(testRepository, never()).save(request1.toEntity());
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