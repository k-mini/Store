package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.domain.Category;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.UserDto.SignUpDto;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:/tableInit.sql")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class AuthApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper om;

    // 회원가입 테스트
//    @Test
    void signup() throws Exception {
        // given
        SignUpDto signupDto = new SignUpDto("kmini", "kmini@gmail.com","1234","1234" , null);
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