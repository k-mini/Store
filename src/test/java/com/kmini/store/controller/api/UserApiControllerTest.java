package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.ApiDocumentUtils;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.config.WithMockCustomUserSecurityContextFactory;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.UserDto.UserSaveReqApiDto;
import com.kmini.store.dto.response.UserDto;
import com.kmini.store.dto.response.UserDto.*;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.Gender.WOMAN;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
    ObjectMapper objectMapper;
    @Autowired
    WithMockCustomUserSecurityContextFactory securityContextFactory;

    // 회원가입 테스트
    @WithAnonymousUser
    @DisplayName("회원 가입 API (유저 생성)")
    @Test
    void saveUser() throws Exception {
        // given
        String username = "kmini";
        String email = "kmini@gmail.com";
        String password = "1234";
        String passwordCheck = "1234";
        String roadAddress = "서울 강남구 가로수길 5";
        String jibunAddress = "서울 강남구 신사동 537-5";
        String detailAddress = "3층";
        UserSaveReqApiDto userSaveReqApiDto =
                new UserSaveReqApiDto(username, email, password, passwordCheck,
                        MAN, LocalDate.of(1987,7,19),
                        20333,roadAddress, jibunAddress, detailAddress, null);
        String requestBody = objectMapper.writeValueAsString(userSaveReqApiDto);
        log.info("requestBody = {}", requestBody);

        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, MediaType.IMAGE_PNG_VALUE, inputStream);

        MockPart filePart = new MockPart("file", fileName + "." + contentType, file.getBytes());
        filePart.getHeaders().setContentType(MediaType.IMAGE_PNG);
        MockPart jsonPart = new MockPart("userSaveReqApiDto", requestBody.getBytes(StandardCharsets.UTF_8));
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
                .andDo(document("user/save-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                requestParts(
                                        partWithName("userSaveReqApiDto").description("회원 가입 JSON"),
                                        partWithName("file").description("회원 썸네일")
                                ),
                                requestPartFields(
                                        "userSaveReqApiDto",
                                        fieldWithPath("email").description("가입할 이메일").optional(),
                                        fieldWithPath("username").description("가입할 회원 유저명"),
                                        fieldWithPath("password").description("가입할 회원 비밀번호"),
                                        fieldWithPath("passwordCheck").description("가입할 회원 비밀번호 확인")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("가입 처리된 유저 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("가입 처리된 유저 ID"),
                                        fieldWithPath("email").description("가입 처리된 이메일"),
                                        fieldWithPath("username").description("가입 처리된 유저명")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MULTIPART_FORM_DATA)
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        UserSaveRespDto userSaveRespDto = objectMapper.treeToValue(jsonNode, UserSaveRespDto.class);
        assertThat(userSaveRespDto.getEmail()).isEqualTo(userSaveReqApiDto.getEmail());
        assertThat(userSaveRespDto.getUsername()).isEqualTo(userSaveReqApiDto.getUsername());
    }

    @WithMockCustomUser
    @DisplayName("회원 수정 API (자신이 자신의 회원정보를 수정)")
    @Test
    void updateUser() throws Exception {
        // given
        // 회원 가져오기
        User user = ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Long userId = user.getId();

        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);

        Map<String, String> map = Map.of("email", user.getEmail(), "username", "kmini2", "password", "abcd", "passwordCheck", "abcd");
        String userUpdateApiReqDto = objectMapper.writeValueAsString(map);
        log.info("userUpdateApiReqDto = {}", userUpdateApiReqDto);

        // part 만들기
        MockPart filePart = new MockPart("file", file.getOriginalFilename(), file.getBytes());
        filePart.getHeaders().setContentType(MediaType.IMAGE_PNG);
        MockPart jsonPart = new MockPart("userUpdateReqApiDto", userUpdateApiReqDto.getBytes(StandardCharsets.UTF_8));
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
                                "user/update-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("회원 수정할 유저 ID")
                                ),
                                requestParts(
                                        partWithName("userUpdateReqApiDto").description("수정할 회원 정보 JSON"),
                                        partWithName("file").description("수정할 파일")
                                ),
                                requestPartFields(
                                        "userUpdateReqApiDto",
                                        fieldWithPath("email").description("이메일 (해당 이메일은 서버 쪽 기술 구현때문에 넣어둔 것입니다. " +
                                                "이메일은 변경되지 않습니다.)").optional(),
                                        fieldWithPath("username").description("수정할 회원 유저명"),
                                        fieldWithPath("password").description("수정할 회원 비밀번호"),
                                        fieldWithPath("passwordCheck").description("수정할 회원 비밀번호 확인")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("회원 수정 처리된 유저 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("회원 수정 처리된 유저 ID"),
                                        fieldWithPath("username").description("회원 수정 처리된 유저명"),
                                        fieldWithPath("email").description("회원 수정 처리된 이메일")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description(MULTIPART_FORM_DATA)
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        UserUpdateRespDto userUpdateRespDto = objectMapper.treeToValue(jsonNode, UserUpdateRespDto.class);

        // 데이터베이스의 객체와 비교
        assertThat(userUpdateRespDto.getUserId()).isEqualTo(user.getId());
        assertThat(userUpdateRespDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userUpdateRespDto.getEmail()).isEqualTo(user.getEmail());
    }

    @WithMockCustomUser
    @DisplayName("회원 조회 API (자신이 자신을 조회)")
    @Test
    void selectUser() throws Exception {

        // given
        // 사전 회원가입
        String username = "kmini3333";
        String password = "1234";
        String email = "kmini22@gmail.com";
        UserRole userRole = UserRole.USER;
        UserStatus userStatus = UserStatus.SIGNUP;
        User user = userService.saveUser(
                new User(username, password, email, userRole, userStatus, null,
                        WOMAN, LocalDate.of(1993,4,5)));
        Long userId = user.getId();


        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/user/{userId}", userId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/select-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("조회할 유저 ID")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("조회된 회원 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("유저 ID"),
                                        fieldWithPath("username").description("유저명"),
                                        fieldWithPath("email").description("이메일")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        UserSelectRespDto userSelectRespDto = objectMapper.treeToValue(jsonNode, UserSelectRespDto.class);

        assertThat(userId).isEqualTo(userSelectRespDto.getId());
        assertThat(username).isEqualTo(userSelectRespDto.getUsername());
        assertThat(email).isEqualTo(userSelectRespDto.getEmail());
    }

    @WithMockCustomUser(role = UserRole.MANAGER)
    @DisplayName("회원 탈퇴 API (매니저 권한 소유)")
    @Test
    void withdrawUser() throws Exception {

        // given
        // 사전 회원가입
        String username = "kmini1235";
        String password = "1234";
        String email = "kmini22@gmail.com";
        UserRole userRole = UserRole.USER;
        UserStatus userStatus = UserStatus.SIGNUP;
        User user = userService.saveUser(
                new User(username, password, email, userRole, userStatus, null
                , WOMAN, LocalDate.of(1997,11,23)));
        Long userId = user.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/user/withdraw-{userId}", userId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user/withdraw-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("탈퇴처리할 유저 ID")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("탈퇴 처리할 회원 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("탈퇴 처리된 유저 ID"),
                                        fieldWithPath("username").description("탈퇴 처리된 유저명"),
                                        fieldWithPath("email").description("탈퇴 처리된 이메일")
                                )
                        )
                ).andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        UserWithDrawRespDto userWithDrawRespDto = objectMapper.treeToValue(jsonNode, UserWithDrawRespDto.class);

        assertThat(userId).isEqualTo(userWithDrawRespDto.getId());
        assertThat(username).isEqualTo(userWithDrawRespDto.getUsername());
        assertThat(email).isEqualTo(userWithDrawRespDto.getEmail());
    }

    @WithMockCustomUser(role = UserRole.ADMIN)
    @DisplayName("회원 데이터 삭제 API (관리자 권한 소유)")
    @Test
    void deleteUser() throws Exception {

        // given
        // 사전 회원가입
        String username = "kmini56789";
        String password = "1234";
        String email = "kmini22@gmail.com";
        UserRole userRole = UserRole.USER;
        UserStatus userStatus = UserStatus.SIGNUP;
        User user = userService.saveUser(
                new User(username, password, email, userRole, userStatus, null,
                        MAN, LocalDate.of(2002, 10, 23)));
        Long userId = user.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/user/{userId}", userId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user/delete-user-api",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("탈퇴처리할 유저 ID")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("삭제 처리할 회원 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("삭제 처리된 유저 ID"),
                                        fieldWithPath("username").description("삭제 처리된 유저명"),
                                        fieldWithPath("email").description("삭제 처리된 이메일")
                                )
                        )
                ).andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        UserDeleteRespDto userDeleteRespDto = objectMapper.treeToValue(jsonNode, UserDeleteRespDto.class);

        assertThat(userId).isEqualTo(userDeleteRespDto.getId());
        assertThat(username).isEqualTo(userDeleteRespDto.getUsername());
        assertThat(email).isEqualTo(userDeleteRespDto.getEmail());


    }
}