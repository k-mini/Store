package com.kmini.store.dto.response;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;

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
        private Integer zonecode;
        private String roadAddress;
        private String jibunAddress;
        private String detailAddress;
        private String createdDate;
        private String lastModifiedDate;

        public UserAPILoginSuccessDto(AccountContext accountContext) {
            User user = accountContext.getUser();
            this.userId = user.getId();
            this.email = user.getEmail();
            this.username = accountContext.getUsername();
            this.password = accountContext.getPassword();
            this.authorities = accountContext.getAuthorities();
            this.userStatus = user.getUserStatus().toString();
            this.thumbnail = user.getThumbnail();
            this.userRole = user.getRole().toString();
            this.gender = user.getGender() != null ? user.getGender().toString() : null;
            this.birthdate = user.getBirthdate() != null ? CustomTimeUtils.convertTime(user.getBirthdate()) : null;
            this.zonecode = user.getZonecode();
            this.roadAddress = user.getRoadAddress();
            this.jibunAddress = user.getJibunAddress();
            this.detailAddress = user.getDetailAddress();
            this.createdDate = CustomTimeUtils.convertTime(user.getCreatedDate());
            this.lastModifiedDate = CustomTimeUtils.convertTime(user.getLastModifiedDate());
            this.roadAddress = user.getRoadAddress();
            this.jibunAddress = user.getJibunAddress();
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
        private Integer zonecode;
        private String roadAddress;
        private String jibunAddress;
        private String detailAddress;
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
                    .zonecode(user.getZonecode())
                    .roadAddress(user.getRoadAddress())
                    .jibunAddress(user.getJibunAddress())
                    .detailAddress(user.getDetailAddress())
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

