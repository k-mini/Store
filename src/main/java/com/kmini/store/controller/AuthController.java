package com.kmini.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping({"/","/index.html","/index"})
    public String home() {
        return "index";
    }

    @GetMapping("/auth/signup")
    public String signup() {
        return "/auth/signup";
    }
    @GetMapping("/auth/signin")
    public String login() {
        return "/auth/signin";
    }
}
