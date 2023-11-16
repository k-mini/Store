package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
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
public class AuthController {

    private final UserService userService;

    @GetMapping({"/","/index"})
    public String home() {
        log.info("메인화면");
        return "index";
    }

    @GetMapping("/auth/signup")
    public String signup(Model model) {
        model.addAttribute("signUpDto",new SignUpDto());
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@Validated @ModelAttribute SignUpDto signUpDto, BindingResult bindingResult) {
        log.debug("signUpDto = {}", signUpDto);
        if (!signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("signUpDto", "password","패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors = {}", bindingResult);
            return "auth/signup";
        }

        userService.save(signUpDto);
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
        model.addAttribute("userUpdateReqDto", UserUpdateReqDto.toDto(user));
        return "/auth/mypage";
    }

    @PostMapping("/auth/my-page")
    public String update(@Validated UserUpdateReqDto userUpdateReqDto, BindingResult bindingResult,
                         @AuthenticationPrincipal PrincipalDetail principal) {
        User user = principal.getUser();
        log.debug("userUpdateReqDto = {}", userUpdateReqDto);

        if (!userUpdateReqDto.getPassword().equals(userUpdateReqDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("userUpdateReqDto", "password","패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors = {}", bindingResult);
            return "/auth/mypage";
        }

        userService.update(user.getId(), userUpdateReqDto);

        return "redirect:/";
    }
}
