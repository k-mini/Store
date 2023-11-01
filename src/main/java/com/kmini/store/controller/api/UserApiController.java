package com.kmini.store.controller.api;


import com.kmini.store.domain.User;
import com.kmini.store.dto.RespDto;
import com.kmini.store.dto.UserUpdateDto;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    // 회원 수정
    @PatchMapping("/{id}")
    public ResponseEntity<UserUpdateDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        UserUpdateDto updateDto = userService.update(id, userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDto);
    }

    // 회원 탈퇴
    @PatchMapping("/withdraw/{id}")
    public ResponseEntity<?> withdraw(@PathVariable Long id) {
        userService.withdraw(id);
        return ResponseEntity.ok(new RespDto<Void>(1,"성공",null));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new RespDto<Void>(1,"성공", null));
    }
}
