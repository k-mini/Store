package com.kmini.store.service.recaptcha;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReCaptChaServiceV3 implements ReCaptChaService{

    @Value("${google.recaptcha.site-key}")
    private String siteKey;
    @Value("${google.recaptcha.secret-key}")
    private String secretKey;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RECAPTCHA_VERIFY_URL_V3 =
            "https://www.google.com/recaptcha/api/siteverify";
    private static final double SCORE_LIMIT = 0.5;

    public boolean resolveReCaptCahToken(String token) {

        if (!StringUtils.hasText(token)) {
            throw new IllegalStateException("reCaptChaToken is not valid");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", token);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, httpHeaders);
        Map<String,Object> result =
                restTemplate.postForObject(RECAPTCHA_VERIFY_URL_V3, request, Map.class);

        boolean success = (boolean) result.get("success");
        double score = (double) result.get("score");

        if (success && score >= SCORE_LIMIT) {
            return true;
        }
        throw new IllegalStateException();
    }
}
