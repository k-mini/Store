package com.kmini.store.controller.api;

import com.kmini.store.dto.RespDto;
import com.kmini.store.dto.SignupDto;
import com.kmini.store.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {
        log.info("signupDto = {}", signupDto);
        authService.signup(signupDto.toEntity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RespDto<>(1,"성공",null));
    }
}
