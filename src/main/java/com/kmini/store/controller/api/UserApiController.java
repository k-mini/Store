package com.kmini.store.controller.api;


import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.UserDto.UserSaveReqDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.dto.response.UserDto.*;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final UserResourceManager userResourceManager;

    // 회원가입
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveUser(@RequestPart UserSaveReqDto userSaveReqDto,
                                      @RequestPart(required = false) MultipartFile file) {
        log.debug("userSaveReqDto = {}", userSaveReqDto);
        log.debug("file = {}", file);

        User user = userService.saveUser(User.builder()
                .email(userSaveReqDto.getEmail())
                .username(userSaveReqDto.getUsername())
                .password(userSaveReqDto.getPassword())
                .thumbnail(userResourceManager.storeFile(userSaveReqDto.getEmail(), file))
                .build());

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
                                        @RequestPart UserUpdateReqDto userUpdateReqDto,
                                        @RequestPart(required = false) MultipartFile file) {
        log.debug("userId = {}, userUpdateReqDto = {}", userId, userUpdateReqDto);
        log.debug("file = {}", file);

        User user = userService.updateUser(userId, userUpdateReqDto, file);

        UserUpdateRespDto result = UserUpdateRespDto.toDto(user);

        return ResponseEntity.ok()
                             .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw/{id}")
    public ResponseEntity<?> withdrawUser(@PathVariable Long id) {
        log.debug("id = {}", id);

        User user = userService.withdrawUser(id);

        UserWithDrawRespDto result = UserWithDrawRespDto.toDto(user);

        return ResponseEntity.ok()
                             .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.debug("id = {}", id);

        User user = userService.deleteUser(id);

        UserDeleteRespDto result = UserDeleteRespDto.toDto(user);

        return ResponseEntity.ok()
                             .body(new CommonRespDto<>(1, "성공", result));
    }
}
