package com.kmini.store.service;

import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(User user) {
        user.setUserStatus(UserStatus.SIGNUP);
        user.setRole(UserRole.USER);
        userRepository.save(user);
        log.info("user = {}", user);
    }
}
