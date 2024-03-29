package com.kmini.store.service.recaptcha;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@Service
@RequiredArgsConstructor
public class ReCaptChaServiceEnterPrise implements ReCaptChaService{

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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        Map<String,Object> result =
                restTemplate
                        .postForObject(RECAPTCHA_VERIFY_URL_ENTERPRISE,
                                httpEntity,
                                Map.class,
                                Map.of(
                                        "key", secretKey,
                                        "projectId", projectId));

        System.out.println("responseObject = " + result);
        return true;
    }
}
