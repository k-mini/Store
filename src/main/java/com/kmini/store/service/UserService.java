package com.kmini.store.service;

import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserResourceManager userResourceManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // 회원가입
    @Transactional
    public User saveUser(User user) {
        userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername())
                .ifPresent(existedUser-> {
                    if (existedUser.getEmail().equals(user.getEmail())) {
                        throw new IllegalArgumentException("이미 존재하는 이메일입니다. email : " + existedUser.getEmail());
                    }
                    if (existedUser.getUsername().equals(user.getUsername())) {
                        throw new IllegalArgumentException("이미 존재하는 유저명입니다. username : " + existedUser.getUsername());
                    }
                });

        // 유저 프로필 설정
        String userProfilePath = userResourceManager.storeFile(user.getUsername(), user.getFile());
        user.setThumbnail(userProfilePath);
        
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        if (user.getUserStatus() == null) {
            user.setUserStatus(UserStatus.SIGNUP);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // 회원 조회
    @Transactional
    public User selectUser(Long id) {
        return userRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    // 회원 수정
    @Transactional
    public User updateUser(User mergingUser) {

        String thumbnailUri = userResourceManager.updateFile(mergingUser.getThumbnail(), mergingUser.getFile());
        mergingUser.setThumbnail(thumbnailUri);
        mergingUser.setPassword(bCryptPasswordEncoder.encode(mergingUser.getPassword()));

        User updateUser = userRepository.save(mergingUser);

        User.updateSecurityContext(updateUser);

        return updateUser;
    }

    // 회원 탈퇴
    @Transactional
    public User withdrawUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        user.setUserStatus(UserStatus.WITHDREW);
        return user;
    }

    // 회원 삭제
    @Transactional
    public User deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.delete(user);
        return user;
    }

    @Transactional
    public Page<User> selectAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    @Transactional
    public User selectUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("이메일이 존재하지 않습니다."));
    }

//    @Transactional
//    public User saveOrUpdate(User user) {
//        return
//    }
}
