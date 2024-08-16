package com.template.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserCreateDto {

    @Builder
    public record Request(@NotNull String email,
                          @NotNull String phone,
                          @NotNull String name,
                          @NotNull String password) {
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
