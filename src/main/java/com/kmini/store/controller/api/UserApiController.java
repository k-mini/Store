package com.kmini.store.controller.api;


import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.UserDto.SignUpDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.dto.response.UserDto.UserUpdateRespDto;
import com.kmini.store.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserServiceImpl userService;
    private final UserResourceManager userResourceManager;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signupDto) {
        userService.save(User.builder()
                             .email(signupDto.getEmail())
                             .username(signupDto.getUsername())
                             .password(signupDto.getPassword())
                             .thumbnail(userResourceManager.storeFile(signupDto.getEmail(), signupDto.getFile()))
                             .build());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", null));
    }

    // 회원 수정
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateReqDto userUpdateReqDto) {
        UserUpdateRespDto result = userService.updateUser(id, userUpdateReqDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw/{id}")
    public ResponseEntity<?> withdraw(@PathVariable Long id) {
        userService.withdraw(id);
        return ResponseEntity.ok(new CommonRespDto<Void>(1, "성공", null));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new CommonRespDto<Void>(1, "성공", null));
    }
}
