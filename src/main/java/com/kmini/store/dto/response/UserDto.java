package com.kmini.store.dto.response;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public class UserDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class UserAPILoginSuccessDto {
        private Long userId;
        private String email;
        private String username;
        private String password;
        private Collection<GrantedAuthority> authorities;
        private String userStatus;
        private String thumbnail;
        private String userRole;
        private String gender;
        private String birthdate;
        private String createdDate;
        private String lastModifiedDate;

        public UserAPILoginSuccessDto(AccountContext accountContext) {
            this.userId = accountContext.getUser().getId();
            this.email = accountContext.getUser().getEmail();
            this.username = accountContext.getUsername();
            this.password = accountContext.getPassword();
            this.authorities = accountContext.getAuthorities();
            this.userStatus = accountContext.getUser().getUserStatus().toString();
            this.thumbnail = accountContext.getUser().getThumbnail();
            this.userRole = accountContext.getUser().getRole().toString();
            this.gender = accountContext.getUser().getGender().toString();
            this.birthdate = CustomTimeUtils.convertTime(accountContext.getUser().getBirthdate());
            this.createdDate = CustomTimeUtils.convertTime(accountContext.getUser().getCreatedDate());
            this.lastModifiedDate = CustomTimeUtils.convertTime(accountContext.getUser().getLastModifiedDate());
        }
    }

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
        private Long userId;
        private String email;
        private String username;
        private Collection<GrantedAuthority> authorities;
        private String userStatus;
        private String thumbnail;
        private String userRole;
        private String gender;
        private String birthdate;
        private String createdDate;
        private String lastModifiedDate;

        public static UserUpdateRespDto toDto(User user) {
            return UserUpdateRespDto.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .userStatus(user.getUserStatus().toString())
                    .thumbnail(user.getThumbnail())
                    .userRole(user.getRole().toString())
                    .gender(user.getGender().toString())
                    .birthdate(CustomTimeUtils.convertTime(user.getBirthdate()))
                    .createdDate(CustomTimeUtils.convertTime(user.getCreatedDate()))
                    .lastModifiedDate(CustomTimeUtils.convertTime(user.getLastModifiedDate()))
                    .build();
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

