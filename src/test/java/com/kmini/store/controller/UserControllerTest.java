package com.kmini.store.controller;

import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.config.auth.CustomUserDetailsService;
import com.kmini.store.dto.request.UserDto;
import com.kmini.store.dto.request.UserDto.SignUpDto;
import com.kmini.store.dto.request.UserDto.UserUpdateReqDto;
import com.kmini.store.service.UserService;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @WithAnonymousUser
    @DisplayName("메인화면 진입")
    @Test
    void home() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/").param("hello","1")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());

    }

    @DisplayName("회원가입 화면 진입")
    @Test
    void getSignup() throws Exception {

        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/auth/signup")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("auth/signup"))
                .andDo(print());
    }

    @DisplayName("회원가입")
    @Test
    void postSignup() throws Exception {

        // given
        String email = "user@gmail.com";
        String username = "user1";
        String password = "1111";
        String passwordCheck = "1111";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        log.info("file={}", file.getOriginalFilename());
        // when
        ResultActions resultActions = mockMvc.perform(
                multipart("/auth/signup")
                        .file(file)
                        .param("email", email)
                        .param("username", username)
                        .param("password", password)
                        .param("passwordCheck", passwordCheck)
        );
        // then
        resultActions.andExpect(redirectedUrl("/auth/signin"));
        resultActions.andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @DisplayName("로그인 화면 진입")
    @Test
    void getLogin() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/auth/signin")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("auth/signin"))
                .andDo(print());
    }

    @WithAnonymousUser
    @DisplayName("로그인 진행")
    @Test
    void login() throws Exception {
        // given
        String email = "test@gmail.com";
        String password = "1111";

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/auth/signin")
                        .param("email", email)
                        .param("password", password)
        );

        // then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("마이페이지 화면 진입")
    @Test
    void getMyPage() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/auth/my-page")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("/auth/mypage"))
                .andDo(print());
    }

    @WithMockCustomUser()
    @DisplayName("마이페이지 화면에서 회원 내용 수정")
    @Test
    void postMyPage() throws Exception {
        // given
        String email = "kmini@gmail.com";
        String username = "kmini2";
        String password = "1111";
        String passwordCheck = "1111";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile thumbnailFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);

        // when
        ResultActions resultActions = mockMvc.perform(
                multipart("/auth/my-page")
                        .file(thumbnailFile)
                        .param("email", email)
                        .param("username", username)
                        .param("password", password)
                        .param("passwordCheck",passwordCheck)
        );

        // then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {

        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


        // then
        // 로그아웃이 되면 게시판 자원에 접근이 불가능하고 로그인 페이지로 이동.
        mockMvc.perform(
                get("/boards/trade/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/auth/signin"))
                .andDo(print());

    }
}