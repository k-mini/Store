package com.kmini.store.dto.response;

import com.kmini.store.domain.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserSaveRespDto {

        private Long id;
        private String username;
        private String email;

        public static UserSaveRespDto toDto(User user) {
            return UserSaveRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserSelectRespDto {

        private Long id;
        private String username;
        private String email;

        public static UserSelectRespDto toDto(User user) {
            return UserSelectRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateRespDto {
        private Long id;
        private String username;
        private String email;

        public static UserUpdateRespDto toDto(User user) {
            return UserUpdateRespDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername()).build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class UserWithDrawRespDto {
        private Long id;
        private String username;
        private String email;

        public static UserWithDrawRespDto toDto(User user) {
            return UserWithDrawRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDeleteRespDto {
        private Long id;
        private String username;
        private String email;

        public static UserDeleteRespDto toDto(User user) {
            return UserDeleteRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
    }
}

