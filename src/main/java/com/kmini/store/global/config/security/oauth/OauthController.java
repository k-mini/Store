package com.kmini.store.global.config.security.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OauthController {

    @GetMapping("/oauth2/callback")
    public Map<String,String> oauthCallback(Map<String,String> params) {
        System.out.println("콜백 성공");
        return params;
    }
}
