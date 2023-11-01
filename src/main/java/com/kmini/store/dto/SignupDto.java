package com.kmini.store.dto;

import com.kmini.store.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDto {

    private String username;

    private String password;

    private String email;

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
