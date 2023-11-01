package com.kmini.store.dto;

import com.kmini.store.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    private String username;

    private String password;

    private String thumbnail;

    public static UserUpdateDto toDto(User user) {
        return UserUpdateDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .thumbnail(user.getThumbnail())
                .build();
    }
}
