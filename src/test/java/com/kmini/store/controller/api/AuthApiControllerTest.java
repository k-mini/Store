package com.kmini.store.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.SignupDto;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("classpath:/tableInit.sql")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class AuthApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper om;

    // 회원가입 테스트
    @Test
    void signup() throws Exception {
        // given
        SignupDto signupDto = new SignupDto("kmini", "1234", "kmini@gmail.com", null);
        String requestBody = om.writeValueAsString(signupDto);
        log.info("requestBody = {}", requestBody);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/signup")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then  (아이디가 있어야 성공)
        resultActions.andExpect(status().isCreated());
    }

}