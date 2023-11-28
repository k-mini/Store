package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper om;

    // 회원가입 테스트
//    @DisplayName("회원가입 테스트")
//    @Test
//    void signup() throws Exception {
//        // given
//        // 사전 회원가입
//        User user = userRepository.save(new User(1L, "kmini", "1234", "kmini@gmail.com", UserRole.USER, UserStatus.SIGNUP, null));
//        // 요청 보낼 JSON 만들기
//        UserUpdateDto updateDto = new UserUpdateDto("kmini2", "abcd", null);
//        String requestBody = om.writeValueAsString(updateDto);
//        log.info("requestBody = {}", requestBody);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(
//                patch("/api/users/1")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // then  (아이디가 수정되어야 성공)
//        resultActions.andExpect(status().isOk());
//        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
//        UserUpdateDto resultDto = om.readValue(contentAsString, UserUpdateDto.class);
//        // 데이터베이스의 객체와 비교
//        Assertions.assertThat(resultDto.getUsername()).isEqualTo(user.getUsername());
//        Assertions.assertThat(resultDto.getPassword()).isEqualTo(user.getPassword());
//        Assertions.assertThat(resultDto.getThumbnail()).isEqualTo(user.getThumbnail());
//    }

}