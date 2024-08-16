package com.template.domain.user.dto;

import com.template.domain.user.entity.AuthorityEntity;
import com.template.domain.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


public class UserCreateDto {

    @Builder
    public record Request(@NotNull String email,
                          @NotNull String phone,
                          @NotNull String name,
                          @NotNull String password) {

        public UserEntity toEntityWithAuthority(AuthorityEntity authorityEntity) {
            return UserEntity.createInstance(email, phone, name, password, authorityEntity);
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
