package com.test.template.domain.test.web;

import com.test.template.domain.test.dto.TestDto;
import com.test.template.domain.test.service.TestService;
import com.test.template.global.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/list")
    public ResponseEntity<Result> findAll() {
        return ResponseEntity.ok(new Result(testService.findAll()));
    }

    @PostMapping
    public ResponseEntity<Result> addTest(@RequestBody TestDto.Request testDto) {
        return ResponseEntity.ok(new Result(testService.save(testDto)));
    }

}
