package com.template.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.template.domain.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private Long id;
        private String email;
        private String phone;
        private String name;
    }

    @Getter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String email;
        private String phone;
        private String name;

        public Response(UserEntity userEntity) {
            this.id = userEntity.getId();
            this.email = userEntity.getEmail();
            this.phone = userEntity.getPhone();
            this.name = userEntity.getName();
        }

        public static Response from(UserEntity userEntity) {
            return new Response(userEntity);
        }

    }
}
