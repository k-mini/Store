package com.kmini.store.dto.response;

import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserDto {



    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class UserUpdateRespDto {
        private Long id;
        private String username;
        private String email;
        private String thumbnail;
    }
}

