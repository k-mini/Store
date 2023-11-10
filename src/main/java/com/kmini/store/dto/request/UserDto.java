package com.kmini.store.dto.request;

import com.kmini.store.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupDto {

        private String username;

        private String password;

        private String email;

        private String thumbnail;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .thumbnail(thumbnail)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateDto {

        private String email;

        private String username;

        private String password;

        private String thumbnail;

        public static UserUpdateDto toDto(User user) {
            return UserUpdateDto.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .thumbnail(user.getThumbnail())
                    .build();
        }
    }
}
