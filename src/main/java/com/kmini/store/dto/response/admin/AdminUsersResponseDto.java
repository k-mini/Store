package com.kmini.store.dto.response.admin;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.User;
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
        private String createdDate;
        private String lastModifiedDate;

        public static AdminUserDto toDto(User user) {
            return AdminUserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .role(user.getRole().toString())
                    .userStatus(user.getUserStatus().toString())
                    .username(user.getUsername())
                    .createdDate(CustomTimeUtils.convertTime(user.getCreatedDate()))
                    .lastModifiedDate(CustomTimeUtils.convertTime(user.getLastModifiedDate()))
                    .build();
        }
    }
}
