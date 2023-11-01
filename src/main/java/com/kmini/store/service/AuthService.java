package com.kmini.store.service;

import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.SignupDto;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupDto signupDto) {
        User user = signupDto.toEntity();
        user.setUserStatus(UserStatus.SIGNUP);
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }
}
