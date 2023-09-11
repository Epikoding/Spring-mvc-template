package com.test.template.domain.test.dto;

import com.test.template.domain.test.entity.TestEntity;
import com.test.template.global.common.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class TestDto {

    @Getter
    public static class Request extends BaseDto {
        private String email;
        private String phone;
        private String name;
        private String password;

        public TestEntity toEntity() {
            return TestEntity.createInstance(email, phone, name, password);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private String id;

        public Response(String id) {
            this.id = id;
        }

        public static Response from(String id) {
            return new Response(id);
        }
    }
}
