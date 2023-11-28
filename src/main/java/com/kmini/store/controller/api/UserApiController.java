package com.kmini.store.controller.api;


import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.UserDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
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

    // 회원가입 (API 방식)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto.SignUpDto signupDto) {
        userService.save(signupDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1,"성공",null));
    }

    // 회원 수정
    @PatchMapping("/{id}")
    public ResponseEntity<UserUpdateReqDto> update(@PathVariable Long id, @RequestBody UserUpdateReqDto userUpdateReqDto) {
        UserUpdateReqDto updateDto = userService.update(id, userUpdateReqDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDto);
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw/{id}")
    public ResponseEntity<?> withdraw(@PathVariable Long id) {
        userService.withdraw(id);
        return ResponseEntity.ok(new CommonRespDto<Void>(1,"성공",null));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new CommonRespDto<Void>(1,"성공", null));
    }
}
