package com.test.template.domain.test.service;

import com.test.template.domain.test.dto.TestDto;
import com.test.template.domain.test.entity.TestEntity;
import com.test.template.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public List<TestDto.Response> findAll() {
        List<TestEntity> entity = testRepository.findAll();

        return entity.stream()
                .map(v -> TestDto.Response.from(v.getId()))
                .toList();
    }

    public TestDto.Response save(TestDto.Request testDto) {
        TestEntity testEntity = testDto.toEntity();
        testRepository.save(testEntity);

        return TestDto.Response.from(testEntity.getId());
    }
}
