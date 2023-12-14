package com.kmini.store.controller;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.UserDto.UserSaveReqDto;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping({"/", "/index"})
    public String home() {
        log.info("메인화면");
        return "index";
    }

    @GetMapping("/auth/signup")
    public String getUserForm(Model model) {
        model.addAttribute("userSaveReqDto", new UserSaveReqDto());
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String saveUser(@Validated @ModelAttribute UserSaveReqDto userSaveReqDto,
                           @RequestPart MultipartFile file, BindingResult bindingResult) {
        log.debug("userSaveReqDto = {}", userSaveReqDto);
        log.debug("file = {}", file);
        if (!userSaveReqDto.getPassword().equals(userSaveReqDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("signUpDto", "password", "패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors = {}", bindingResult);
            return "auth/signup";
        }

        userService.saveUser(User.builder()
                                .email(userSaveReqDto.getEmail())
                                .username(userSaveReqDto.getUsername())
                                .password(userSaveReqDto.getPassword())
                                .file(file)
                                .build());

        return "redirect:/auth/signin";
    }

    @GetMapping("/auth/signin")
    public String getLoginForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/my-page")
    public String getMyPage(@AuthenticationPrincipal AccountContext accountContext, Model model) {
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

//        userService.updateUser(user.getId(), userUpdateReqDto, file);
        userService.updateUser(User.builder()
                                .id(user.getId())
                                .username(userUpdateReqDto.getUsername())
                                .password(userUpdateReqDto.getPassword())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .userStatus(user.getUserStatus())
                                .file(userUpdateReqDto.getFile())
                                .build());
        return "redirect:/";
    }
}
