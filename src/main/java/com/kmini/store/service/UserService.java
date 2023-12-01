package com.kmini.store.service;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.dto.response.UserDto.UserUpdateRespDto;
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

    private final UserResourceManager userResourceManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    @Transactional
    public User save(User user) {
        user.setRole(UserRole.USER);
        user.setUserStatus(UserStatus.SIGNUP);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 회원 수정
    @Transactional
    public UserUpdateRespDto updateUser(Long userId, UserUpdateReqDto userUpdateReqDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 발견되지 않았습니다."));
        user.setUsername(userUpdateReqDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userUpdateReqDto.getPassword()));
        user.setThumbnail(userResourceManager.updateFile(user.getThumbnail(), userUpdateReqDto.getThumbnailFile()));

        updateSecurityContext(user);

        return UserUpdateRespDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .thumbnail(user.getThumbnail()).build();
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
}
