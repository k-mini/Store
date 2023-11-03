package com.kmini.store.controller;

import com.kmini.store.domain.User;
import com.kmini.store.dto.SignupDto;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping({"/","/index.html","/index"})
    public String home() {
        log.info("메인화면");
        return "index";
    }

    @GetMapping("/auth/signup")
    public String signup() {
        return "/auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(SignupDto signUpDto) {
        log.info("signUpDto : {}", signUpDto);
        User user = signUpDto.toEntity();
        userService.save(user);
        return "/auth/signin";
    }

    @GetMapping("/auth/signin")
    public String login() {
        return "/auth/signin";
    }
}
