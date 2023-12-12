package com.kmini.store.dto.response;

import com.kmini.store.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {


    @Data
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

    @Data
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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateRespDto {
        private Long id;
        private String username;
        private String email;
        private String thumbnail;

        public static UserUpdateRespDto toDto(User user) {
            return UserUpdateRespDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .thumbnail(user.getThumbnail()).build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserWithDrawRespDto {
        private Long id;
        private String username;
        private String email;
        private String thumbnail;

        public static UserWithDrawRespDto toDto(User user) {
            return UserWithDrawRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .thumbnail(user.getThumbnail())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDeleteRespDto {
        private Long id;
        private String username;
        private String email;
        private String thumbnail;

        public static UserDeleteRespDto toDto(User user) {
            return UserDeleteRespDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .thumbnail(user.getThumbnail())
                    .build();
        }
    }
}

