package com.kmini.store.domain.recaptcha.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.domain.recaptcha.service.ReCaptChaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@Service
@RequiredArgsConstructor
public class ReCaptChaServiceEnterPrise implements ReCaptChaService {

    @Value("${google.recaptcha.site-key}")
    private String siteKey;
    @Value("${google.recaptcha.secret-key}")
    private String secretKey;
    @Value("${google.recaptcha.project-id}")
    private String projectId;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RECAPTCHA_VERIFY_URL_ENTERPRISE =
            "https://recaptchaenterprise.googleapis.com/v1/projects/${projectId}/assessments?key=${secretKey}";

    @Override
    public boolean resolveReCaptCahToken(String token) {

        if (!StringUtils.hasText(token)) {
            throw new IllegalStateException("reCaptChaToken is not valid");
        }

        Map<String, Object> params = new HashMap<>();
        Map<String, String> event = new HashMap<>();

        event.put("token", token);
        event.put("expectedAction", "login");
        event.put("siteKey", siteKey);
        params.put("event", event);

        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String uri = UriComponentsBuilder
                .fromHttpUrl(RECAPTCHA_VERIFY_URL_ENTERPRISE)
                .buildAndExpand(Map.of("projectId",projectId ,"secretKey",secretKey))
                .toUriString();

        RequestEntity<Object> request = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody);

        ResponseEntity<Map<String, Object>> result =
                restTemplate.exchange(request, new ParameterizedTypeReference<Map<String, Object>>() {});

        System.out.println("responseBody = " + result.getBody());
        return true;
    }
}
