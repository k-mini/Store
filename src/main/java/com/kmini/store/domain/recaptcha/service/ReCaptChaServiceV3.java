package com.kmini.store.domain.recaptcha.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReCaptChaServiceV3 implements ReCaptChaService {

    @Value("${google.recaptcha.site-key}")
    private String siteKey;
    @Value("${google.recaptcha.secret-key}")
    private String secretKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RECAPTCHA_VERIFY_URL_V3 =
            "https://www.google.com/recaptcha/api/siteverify";
    private static final double SCORE_LIMIT = 0.5;

    public boolean resolveReCaptCahToken(String token) {

        if (!StringUtils.hasText(token)) {
            throw new IllegalStateException("reCaptChaToken is not valid");
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", token);

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(RECAPTCHA_VERIFY_URL_V3)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params);

        ResponseEntity<Map<String,Object>> result =
                restTemplate.exchange(request, new ParameterizedTypeReference<Map<String,Object>>() {});
        Map<String,Object> map = result.getBody();

        boolean success = (boolean) map.get("success");
        double score = (double) map.get("score");

        if (success && score >= SCORE_LIMIT) {
            return true;
        }
        throw new IllegalStateException();
    }
}
