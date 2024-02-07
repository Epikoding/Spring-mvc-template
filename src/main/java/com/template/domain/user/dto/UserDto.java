package com.template.domain.user.dto;

import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.global.common.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserDto {

    @Getter
    public static class Request extends BaseDto {
        private String email;
        private String phone;
        private String name;
        private String password;

        public UserEntity toEntityWithAuthority(AuthorityEntity authorityEntity) {
            return UserEntity.createInstance(email, phone, name, password, authorityEntity);
        }

        public UserEntity toEntity() {
            return UserEntity.createInstance(email, phone, name, password);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long id;

        public Response(Long id) {
            this.id = id;
        }

        public static Response from(Long id) {
            return new Response(id);
        }
    }
}
