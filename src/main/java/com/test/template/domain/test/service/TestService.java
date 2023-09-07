package com.test.template.domain.test.service;

import com.test.template.domain.test.dto.TestDto;
import com.test.template.domain.test.entity.TestEntity;
import com.test.template.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final ModelMapper modelMapper;
    private final TestRepository testRepository;

    public List<TestDto> findAll() {
        List<TestEntity> entity = testRepository.findAll();

        return entity.stream()
                .map(user -> modelMapper.map(user, TestDto.class))
                .toList();
    }
}
