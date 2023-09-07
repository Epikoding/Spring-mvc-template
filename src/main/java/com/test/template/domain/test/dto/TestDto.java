package com.test.template.domain.test.dto;

import com.test.template.global.common.BaseDto;
import lombok.*;


@Getter
@AllArgsConstructor
public class TestDto extends BaseDto {
    public static class Request {
        private String test;
    }

    public static class Response {
        private String test;
    }
}
