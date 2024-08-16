package com.template.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import com.template.global.common.BaseDto;
import com.template.global.common.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class UserDto {

    @Getter
    public static class Request extends BaseDto {
        private final String email;
        private final String phone;
        private final String name;
        private final String password;

        @Builder
        public Request(String uuid, String email, String phone, String name, String password) {
            super();
            setId(uuid);
            this.email = email;
            this.phone = phone;
            this.name = name;
            this.password = password;
        }

        public UserEntity toEntity() {
            return UserEntity.createInstance(email, phone, name, password);
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private String email;
        private String phone;
        private String name;
        private List<Role> roleList;

        public Response(Long id) {
            this.id = id;
        }

        public Response(String email, String phone, String name, List<Role> roleList) {
            this.email = email;
            this.phone = phone;
            this.name = name;
            this.roleList = roleList;
        }

        public Response(UserEntity userEntity) {
            this.email = userEntity.getEmail();
            this.phone = userEntity.getPhone();
            this.name = userEntity.getName();
        }


        public static Response from(Long id) {
            return new Response(id);
        }

        public static Response from(UserEntity userEntity) {
            return new Response(userEntity);
        }
    }
}
