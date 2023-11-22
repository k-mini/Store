package com.kmini.store.service;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.file.ResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.UserDto.SignUpDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ResourceManager resourceManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    @Transactional
    public void save(SignUpDto signUpDto) {
        User user = signUpDto.toEntity(resourceManager);
        user.setRole(UserRole.USER);
        user.setUserStatus(UserStatus.SIGNUP);
        user.setPassword(encryptPassword(user.getPassword()));
        userRepository.save(user);
    }

    // 회원 수정
    @Transactional
    public UserUpdateReqDto update(Long id, UserUpdateReqDto userUpdateReqDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저가 발견되지 않았습니다."));
        user.setUsername(userUpdateReqDto.getUsername());
        user.setPassword(encryptPassword(userUpdateReqDto.getPassword()));
        user.setThumbnail(userUpdateReqDto.getThumbnail());

        updateSecurityContext(user);
        return UserUpdateReqDto.toDto(user);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        user.setUserStatus(UserStatus.WITHDREW);
    }
    // 회원 삭제
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    private void updateSecurityContext(User user) {
        AccountContext accountContext = new AccountContext(user);
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(accountContext, null, accountContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
