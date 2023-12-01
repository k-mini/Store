package com.kmini.store.controller;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.UserDto.SignUpDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserResourceManager userResourceManager;
    private final UserService userService;

    @GetMapping({"/", "/index"})
    public String home() {
        log.info("메인화면");
        return "index";
    }

    @GetMapping("/auth/signup")
    public String saveUserForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String saveUser(@Validated @ModelAttribute SignUpDto signUpDto, BindingResult bindingResult) {
        log.debug("signUpDto = {}", signUpDto);
        if (!signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("signUpDto", "password", "패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors = {}", bindingResult);
            return "auth/signup";
        }

        User user = User.builder()
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .thumbnail(userResourceManager.storeFile(signUpDto.getEmail(), signUpDto.getFile()))
                .build();
        userService.save(user);
        return "redirect:/auth/signin";
    }

    @GetMapping("/auth/signin")
    public String login() {
        return "auth/signin";
    }

    @GetMapping("/auth/my-page")
    public String myPage(@AuthenticationPrincipal AccountContext accountContext, Model model) {
        User user = accountContext.getUser();
        log.debug("user = {}", user);
        model.addAttribute("userUpdateReqDto", UserUpdateReqDto.getUserUpdateForm(user));
        return "/auth/mypage";
    }

    @PostMapping("/auth/my-page")
    public String updateUser(@Validated UserUpdateReqDto userUpdateReqDto, BindingResult bindingResult,
                             @AuthenticationPrincipal AccountContext accountContext) {
        User user = accountContext.getUser();
        log.debug("userUpdateReqDto = {}", userUpdateReqDto);

        if (!userUpdateReqDto.getPassword().equals(userUpdateReqDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("userUpdateReqDto", "password", "패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors = {}", bindingResult);
            return "/auth/mypage";
        }

        userService.updateUser(user.getId(), userUpdateReqDto);
        return "redirect:/";
    }
}
