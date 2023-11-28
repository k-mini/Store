package com.kmini.store.dto.request;

import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@RequiredArgsConstructor
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

        private MultipartFile file;

        public User toEntity(UserResourceManager userResourceManager) {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .thumbnail(userResourceManager.storeFile(email, file))
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateReqDto {

        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private String thumbnail;

        public static UserUpdateReqDto toDto(User user) {
            return UserUpdateReqDto.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .thumbnail(user.getThumbnail())
                    .build();
        }
    }
}
