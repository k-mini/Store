package com.kmini.store.service;

import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.UserUpdateDto;
import com.kmini.store.ex.CustomUserNotFoundException;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 수정
    @Transactional
    public UserUpdateDto update(Long id, UserUpdateDto userUpdateDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new CustomUserNotFoundException("유저가 발견되지 않았습니다."));
        String username = userUpdateDto.getUsername();
        if (StringUtils.hasText(username)) {
            user.setUsername(username);
        }
        String password=  userUpdateDto.getPassword();
        if (StringUtils.hasText(password)) {
            user.setPassword(password);
        }
        String thumbnail = userUpdateDto.getThumbnail();
        if (StringUtils.hasText(thumbnail)) {
            user.setThumbnail(thumbnail);
        }
        return UserUpdateDto.toDto(user);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomUserNotFoundException("유저를 찾을 수 없습니다."));
        user.setUserStatus(UserStatus.WITHDREW);
    }

    // 회원 삭제
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomUserNotFoundException("유저를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}
