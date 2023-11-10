package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.UserDto;
import com.kmini.store.dto.request.UserDto.SignupDto;
import com.kmini.store.dto.request.UserDto.UserUpdateDto;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping({"/","/index"})
    public String home() {
        log.info("메인화면");
        return "index";
    }

    @GetMapping("/auth/signup")
    public String signup() {
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(SignupDto signUpDto) {
        log.info("signUpDto : {}", signUpDto);
        User user = signUpDto.toEntity();
        userService.save(user);
        return "redirect:/auth/signin";
    }

    @GetMapping("/auth/signin")
    public String login() {
        return "auth/signin";
    }

    @GetMapping("/auth/my-page")
    public String myPage(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
        User user = principal.getUser();
        log.debug("user = {}", user);
        model.addAttribute("userUpdateDto", UserUpdateDto.toDto(user));
        return "auth/myPage";
    }

    @PostMapping("/auth/my-page")
    public String update(UserUpdateDto userUpdateDto, @AuthenticationPrincipal PrincipalDetail principal) {
        User user = principal.getUser();
        log.debug("userUpdateDto = {}", userUpdateDto);
        log.debug("user = {}", user);

        userService.update(user.getId(), userUpdateDto);

        return "redirect:/";
    }
}
