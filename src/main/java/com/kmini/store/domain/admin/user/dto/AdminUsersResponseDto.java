package com.kmini.store.domain.admin.user.dto;

import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Getter
@Setter
public class AdminUsersResponseDto<T> {

    int draw;
    Page<T> page;

    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class AdminUserDto {
        private Long id;
        private String email;
        private String role;
        private String userStatus;
        private String username;
        private String gender;
        private String birthdate;
        private String roadAddress;
        private String jibunAddress;
        private String createdDate;
        private String lastModifiedDate;

        public static AdminUserDto toDto(User user) {
            return AdminUserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .role(user.getRole().toString())
                    .userStatus(user.getUserStatus().toString())
                    .username(user.getUsername())
                    .gender(user.getGender().toString())
                    .birthdate(CustomTimeUtils.convertTime(user.getBirthdate()))
                    .roadAddress(user.getRoadAddress())
                    .jibunAddress(user.getJibunAddress())
                    .createdDate(CustomTimeUtils.convertTime(user.getCreatedDate()))
                    .lastModifiedDate(CustomTimeUtils.convertTime(user.getLastModifiedDate()))
                    .build();
        }
    }
}
