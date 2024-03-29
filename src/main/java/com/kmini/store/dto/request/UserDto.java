package com.kmini.store.dto.request;

import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UserDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class UserAPILoginDto {
        private String email;
        private String password;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSaveReqDto {

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private Gender gender;

        private LocalDate birthdate;

        private MultipartFile file;

        private String roadAddress;
        private String jibunAddress;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSaveReqApiDto {

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private Gender gender;

        private LocalDate birthdate;

        private Integer zonecode;
        private String roadAddress;
        private String jibunAddress;
        private String detailAddress;
        private String reCaptChaToken;

        public static User toUser(UserSaveReqApiDto userSaveReqApiDto, MultipartFile file) {
            return User.builder()
                    .email(userSaveReqApiDto.getEmail())
                    .username(userSaveReqApiDto.getUsername())
                    .password(userSaveReqApiDto.getPassword())
                    .gender(userSaveReqApiDto.getGender())
                    .birthdate(userSaveReqApiDto.getBirthdate())
                    .zonecode(userSaveReqApiDto.getZonecode())
                    .roadAddress(userSaveReqApiDto.getRoadAddress())
                    .jibunAddress(userSaveReqApiDto.getJibunAddress())
                    .detailAddress(userSaveReqApiDto.getDetailAddress())
                    .file(file)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateReqDto {

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private Gender gender;

        private LocalDate birthdate;

        private MultipartFile file;

        private Integer zonecode;
        private String roadAddress;
        private String jibunAddress;
        private String detailAddress;

        public static UserUpdateReqDto getUserUpdateForm(User user) {
            return UserUpdateReqDto.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .gender(user.getGender())
                    .birthdate(user.getBirthdate())
                    .zonecode(user.getZonecode())
                    .roadAddress(user.getRoadAddress())
                    .jibunAddress(user.getJibunAddress())
                    .detailAddress(user.getDetailAddress())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserUpdateReqApiDto {

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        private String email;

        @NotBlank(message ="이름은 반드시 입력해야 합니다.")
        private String username;

        @NotBlank(message= "패스워드는 반드시 입력해야 합니다.")
        private String password;

        private String passwordCheck;

        private Gender gender;

        private LocalDate birthdate;

        private Integer zonecode;
        private String roadAddress;
        private String jibunAddress;
        private String detailAddress;

        public static User toUser(UserUpdateReqApiDto userUpdateReqApiDto, User user, MultipartFile file) {
            return User.builder()
                    .id(user.getId())
                    .username(userUpdateReqApiDto.getUsername())
                    .password(userUpdateReqApiDto.getPassword())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .userStatus(user.getUserStatus())
                    .file(file)
                    .createdDate(user.getCreatedDate())
                    .gender(userUpdateReqApiDto.getGender())
                    .birthdate(userUpdateReqApiDto.getBirthdate())
                    .zonecode(userUpdateReqApiDto.getZonecode())
                    .roadAddress(userUpdateReqApiDto.getRoadAddress())
                    .jibunAddress(userUpdateReqApiDto.getJibunAddress())
                    .detailAddress(userUpdateReqApiDto.getDetailAddress())
                    .build();
        }
    }
}
