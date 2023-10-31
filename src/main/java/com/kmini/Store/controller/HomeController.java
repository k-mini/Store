package com.kmini.Store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping({"/","/index.html","/index"})
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
