package com.kmini.store.controller.api;

import com.kmini.store.dto.SignupDto;
import com.kmini.store.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<SignupDto> signup(@RequestBody SignupDto signupDto) {
        log.info("signupDto = {}", signupDto);
        authService.signup(signupDto);
        return ResponseEntity.ok(null);
    }
}
