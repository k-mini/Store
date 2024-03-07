package com.kmini.store.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.UserDto.UserSaveReqApiDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqApiDto;
import com.kmini.store.dto.response.UserDto.*;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final ObjectMapper om;

    // 회원가입
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveUser(@RequestPart UserSaveReqApiDto userSaveReqApiDto,
                                      @RequestPart(required = false) MultipartFile file) {
        log.debug("userSav  eReqApiDto = {}", userSaveReqApiDto);
        log.debug("file = {}", file);

        User newUser = User.builder()
                .email(userSaveReqApiDto.getEmail())
                .username(userSaveReqApiDto.getUsername())
                .password(userSaveReqApiDto.getPassword())
                .gender(userSaveReqApiDto.getGender())
                .birthdate(userSaveReqApiDto.getBirthdate())
                .file(file)
                .build();

        User user = userService.saveUser(newUser);

        UserSaveRespDto result = UserSaveRespDto.toDto(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> selectUser(@PathVariable Long userId) {
        log.debug("userId = {}", userId);

        User user = userService.selectUser(userId);

        UserSelectRespDto result = UserSelectRespDto.toDto(user);

        return ResponseEntity.ok()
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                        @RequestPart UserUpdateReqApiDto userUpdateReqApiDto,
                                        @RequestPart(required = false) MultipartFile file,
                                        @AuthenticationPrincipal AccountContext accountContext) {
        log.debug("userId = {}, userUpdateReqApiDto = {}", userId, userUpdateReqApiDto);
        log.debug("file = {}", file);
        User user = accountContext.getUser();

        if (user.getRole().equals(UserRole.ADMIN) && !user.getId().equals(userId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        User updatedUser = userService.updateUser(User.builder()
                .id(user.getId())
                .username(userUpdateReqApiDto.getUsername())
                .password(userUpdateReqApiDto.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .userStatus(user.getUserStatus())
                .file(file)
                .createdDate(user.getCreatedDate())
                .gender(userUpdateReqApiDto.getGender())
                .birthdate(userUpdateReqApiDto.getBirthdate())
                .build());

        UserUpdateRespDto result = UserUpdateRespDto.toDto(updatedUser);

        return ResponseEntity.ok()
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw-{id}")
    public ResponseEntity<?> withdrawUser(@PathVariable Long id, @AuthenticationPrincipal AccountContext accountContext) {
        log.debug("id = {}", id);
        User user = accountContext.getUser();

        if (!user.getRole().equals(UserRole.MANAGER)) {
            throw new IllegalStateException("매니저 권한이 없습니다.");
        }

        User canceledUser = userService.withdrawUser(id);

        UserWithDrawRespDto result = UserWithDrawRespDto.toDto(canceledUser);

        return ResponseEntity.ok()
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal AccountContext accountContext) {
        log.debug("userId = {}", userId);
        User user = accountContext.getUser();

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        User deletedUser = userService.deleteUser(userId);

        UserDeleteRespDto result = UserDeleteRespDto.toDto(deletedUser);

        return ResponseEntity.ok()
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 인증 정보 얻기
    @GetMapping("/authentication")
    public ResponseEntity<?> getAuthentication() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountContext accountContext = (AccountContext) authentication.getPrincipal();
        clearPassword(accountContext);

        UserAPILoginSuccessDto userAPILoginSuccessDto = new UserAPILoginSuccessDto(accountContext);

        return ResponseEntity.ok(om.writeValueAsString(userAPILoginSuccessDto));
    }

    private void clearPassword(AccountContext accountContext) {
        accountContext.getUser().setPassword(null);
        accountContext.eraseCredentials();
    }
}
