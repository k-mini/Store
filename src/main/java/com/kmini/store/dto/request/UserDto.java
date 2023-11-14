package com.kmini.store.dto.request;

import com.kmini.store.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto {
        
        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

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

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private String thumbnail;

        public static UserUpdateDto toDto(User user) {
            return UserUpdateDto.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .thumbnail(user.getThumbnail())
                    .build();
        }
    }
}
