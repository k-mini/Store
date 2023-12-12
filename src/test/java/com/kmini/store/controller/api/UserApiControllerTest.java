package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.UserDto.UserSaveReqDto;
import com.kmini.store.dto.response.UserDto.UserSaveRespDto;
import com.kmini.store.dto.response.UserDto.UserUpdateRespDto;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:/tableInit.sql")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Slf4j
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper om;

    // 회원가입 테스트
    @WithAnonymousUser
    @DisplayName("회원 가입 API")
    @Test
    void saveUser() throws Exception {
        // given
        String username = "kmini";
        String email = "kmini@gmail.com";
        String password = "1234";
        String passwordCheck = "1234";
        UserSaveReqDto userSaveReqDto = new UserSaveReqDto(username, email, password, passwordCheck);
        String requestBody = om.writeValueAsString(userSaveReqDto);
        log.info("requestBody = {}", requestBody);

        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, MediaType.IMAGE_PNG_VALUE, inputStream);

        MockPart filePart = new MockPart("file", fileName + "." + contentType, file.getBytes());
        filePart.getHeaders().setContentType(MediaType.IMAGE_PNG);
        MockPart jsonPart = new MockPart("userSaveReqDto", requestBody.getBytes(StandardCharsets.UTF_8));
        jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/user")
                        .file(file)
//                        .part(filePart)
                        .part(jsonPart)
                        );

        // then  (아이디가 있어야 성공)
        String result = resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("save-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                requestParts(
                                        partWithName("userSaveReqDto").description("회원 가입 JSON"),
                                        partWithName("file").description("회원의 사진")
                                        ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.id").description("가입 처리된 유저 ID"),
                                        fieldWithPath("data.email").description("가입 처리된 이메일"),
                                        fieldWithPath("data.username").description("가입 처리된 유저명")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = om.readTree(result).get("data");
        UserSaveRespDto userSaveRespDto = om.treeToValue(jsonNode, UserSaveRespDto.class);
        assertThat(userSaveRespDto.getEmail()).isEqualTo(userSaveReqDto.getEmail());
        assertThat(userSaveRespDto.getUsername()).isEqualTo(userSaveReqDto.getUsername());
    }

    @WithMockCustomUser
    @DisplayName("회원 수정")
    @Test
    void updateUser() throws Exception {
        // given
        // 사전 회원가입
        User user = userService.saveUser(
                new User("kmini", "1234", "kmini22@gmail.com", UserRole.USER, UserStatus.SIGNUP, null));
        Long userId = user.getId();

        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);

        Map map = Map.of("username", "kmini2", "password", "abcd", "passwordCheck", "abcd","file", file.getBytes());
        String userUpdateReqDto = om.writeValueAsString(map);
        log.info("userUpdateReqDto = {}", userUpdateReqDto);

        // part 만들기
        MockPart filePart = new MockPart("file", file.getOriginalFilename(), file.getBytes());
        filePart.getHeaders().setContentType(MediaType.IMAGE_PNG);
        MockPart jsonPart = new MockPart("userUpdateReqDto", userUpdateReqDto.getBytes(StandardCharsets.UTF_8));
        jsonPart.getHeaders().setContentType(APPLICATION_JSON);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/user/{userId}", userId)
                        .part(filePart, jsonPart)
                        .with(new RequestPostProcessor() {
                            @Override
                            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                                request.setMethod(HttpMethod.PATCH.toString());
                                return request;
                            }
                        }));

        // then  (아이디가 수정되어야 성공)
        String result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "update-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("회원 수정할 유저 ID")
                                ),
                                requestParts(
                                        partWithName("userUpdateReqDto").description("회원 수정 JSON"),
                                        partWithName("file").description("회원의 사진")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.id").description("회원 수정 처리된 유저 ID"),
                                        fieldWithPath("data.username").description("회원 수정 처리된 유저명"),
                                        fieldWithPath("data.email").description("회원 수정 처리된 이메일"),
                                        fieldWithPath("data.thumbnail").description("회원 수정 처리된 썸네일 파일")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = om.readTree(result).get("data");
        UserUpdateRespDto userUpdateRespDto = om.treeToValue(jsonNode, UserUpdateRespDto.class);

        // 데이터베이스의 객체와 비교
        assertThat(userUpdateRespDto.getId()).isEqualTo(user.getId());
        assertThat(userUpdateRespDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userUpdateRespDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userUpdateRespDto.getThumbnail()).isEqualTo(user.getThumbnail());
    }

}