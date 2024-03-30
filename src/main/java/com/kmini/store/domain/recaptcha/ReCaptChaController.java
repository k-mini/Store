package com.kmini.store.domain.recaptcha;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kmini.store.domain.recaptcha.service.ReCaptChaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReCaptChaController {

    private final ReCaptChaService reCaptChaService;

    // 테스트 api
    @PostMapping("/api/recaptcha")
    public ResponseEntity<?> testAPI(
            @RequestParam Map<String, String> parameters)
            throws JsonProcessingException {
        System.out.println("parameters = " + parameters);
        String token = parameters.get("token");

        // reCAPTCHA V3 버전
        boolean result = reCaptChaService.resolveReCaptCahToken(token);
        // reCAPTCHA ENTERPRISE 버전
//        resolveReCaptChaEnterprise(token);

        return ResponseEntity.ok(result);
    }
}
