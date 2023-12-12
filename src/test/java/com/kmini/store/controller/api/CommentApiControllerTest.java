package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.ApiDocumentUtils;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentUpdateRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplySaveRespDto;
import com.kmini.store.service.CommentService;
import com.kmini.store.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:/tableInit.sql")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
@Transactional
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
class CommentApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    EntityManager em;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ItemBoardService itemBoardService;
    @Autowired
    CommentService commentService;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트를 위한 게시물 작성 (유저 1이 작성)
        String category = "trade";
        String subCategory = "electronics";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardFormSaveDto formSaveDto = new ItemBoardFormSaveDto(subCategory, "Life is Good", "what is your favorite food?", existingFile, null);
        ItemBoard itemBoard = itemBoardService.save(formSaveDto);
        log.info("itemBoard id = {}", itemBoard.getId());
        em.clear();
    }

    @WithMockCustomUser
    @DisplayName("댓글 저장")
    @Test
    void saveComment() throws Exception {
        //given
        User user = User.getSecurityContextUser();
        Long boardId = 1L;
        String content = "댓글 내용입니다~";

        BoardCommentSaveReqDto boardCommentSaveReqDto = new BoardCommentSaveReqDto(boardId, content);
        String requestBody = objectMapper.writeValueAsString(boardCommentSaveReqDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/comment")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
        );

        //then
        String result = resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andDo(document("comment/save-comment",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("boardId").description("작성할 댓글의 게시물 Id"),
                                        fieldWithPath("content").description("새로 작성할 댓글 내용")
                                )
                                ,
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.id").description("댓글 Id"),
                                        fieldWithPath("data.commentUserId").description("작성자 Id"),
                                        fieldWithPath("data.commentUserName").description("작성자 이름"),
                                        fieldWithPath("data.content").description("댓글 내용"),
                                        fieldWithPath("data.replies").description("하위 댓글"),
                                        fieldWithPath("data.createdDate").description("생성 날짜")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardCommentUpdateRespDto boardCommentDto = objectMapper.treeToValue(jsonNode, BoardCommentUpdateRespDto.class);
        assertThat(boardCommentDto.getCommentUserId()).isEqualTo(user.getId());
        assertThat(boardCommentDto.getCommentUserName()).isEqualTo(user.getUsername());
        assertThat(boardCommentDto.getContent()).isEqualTo(content);
    }

    @WithMockCustomUser
    @DisplayName("댓글 수정")
    @Test
    void updateComment() throws Exception {
        // given
        User user = User.getSecurityContextUser();
        BoardCommentUpdateRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();
        Long boardId = 1L;
        String content = "수정내용내용";

        BoardCommentUpdateReqDto boardCommentUpdateReqDto = new BoardCommentUpdateReqDto(boardId, content);
        String requestBody = objectMapper.writeValueAsString(boardCommentUpdateReqDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/comment/{commentId}", commentId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
        );

        //then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document("comment/update-comment",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("commentId").description("수정할 댓글의 Id")
                                ),
                                requestFields(
                                        fieldWithPath("boardId").description("수정할 댓글의 게시물 Id"),
                                                fieldWithPath("content").description("수정할 댓글의 내용")
                                        ),
                                responseFields(
                                        Attributes.attributes(key("title").value("example for title")),
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.id").description("수정된 댓글의 Id"),
                                        fieldWithPath("data.commentUserId").description("수정된 댓글의 작성 유저 Id"),
                                        fieldWithPath("data.commentUserName").description("수정된 댓글의 작성 유저 이름"),
                                        fieldWithPath("data.content").description("수정된 댓글 내용"),
                                        fieldWithPath("data.replies").description("수정된 댓글의 대댓글 배열"),
                                        fieldWithPath("data.createdDate").description("수정된 시간")
                                ),
                                responseFields(
                                        // withSubsectionId : 스니펫 이름 지정
                                        // beneathPath 경로 아래를 명세한다.
                                        beneathPath("data").withSubsectionId("resultData"),
                                        Attributes.attributes(key("title").value("example for title")),
                                        fieldWithPath("id").description("수정된 댓글의 Id").attributes(key("etc").value("this is etc")),
                                        fieldWithPath("commentUserId").description("수정된 댓글의 작성 유저 Id"),
                                        fieldWithPath("commentUserName").description("수정된 댓글의 작성 유저 이름"),
                                        fieldWithPath("content").description("수정된 댓글 내용"),
                                        fieldWithPath("replies").description("수정된 댓글의 대댓글 배열"),
                                        fieldWithPath("createdDate").description("수정된 시간")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardCommentUpdateRespDto boardCommentDto = objectMapper.treeToValue(jsonNode, BoardCommentUpdateRespDto.class);
        assertThat(boardCommentDto.getCommentUserId()).isEqualTo(user.getId());
        assertThat(boardCommentDto.getCommentUserName()).isEqualTo(user.getUsername());
        assertThat(boardCommentDto.getContent()).isEqualTo(content);
    }

    @WithMockCustomUser
    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() throws Exception {
        // given
        User user = User.getSecurityContextUser();
        BoardCommentUpdateRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/comment/" + commentId)
        );

        //then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String code = objectMapper.readTree(result).get("code").asText();
        String message = objectMapper.readTree(result).get("message").asText();
        assertThat(code).isEqualTo("1");
        assertThat(message).isEqualTo("성공");
    }

    @WithMockCustomUser
    @DisplayName("대댓글 저장")
    @Test
    void saveReply() throws Exception {
        // given
        User user = User.getSecurityContextUser();
        BoardCommentUpdateRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();
        Long boardId = 1L;
        String content = "대댓글내용입니다~";

        BoardReplySaveReqDto boardReplySaveReqDto = new BoardReplySaveReqDto(boardId, commentId, content);
        String requestBody = objectMapper.writeValueAsString(boardReplySaveReqDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/comment/{commentId}/reply", commentId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
        );

        //then
        String result = resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                                "comment/save-reply",
                                getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("boardId").description("댓글을 작성한 게시물 Id"),
                                        fieldWithPath("topCommentId").description("대댓글의 부모 댓글 Id"),
                                        fieldWithPath("content").description("대댓글 내용")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.topCommentId").description("대댓글의 부모 댓글 Id"),
                                        fieldWithPath("data.replyId").description("대댓글의 Id"),
                                        fieldWithPath("data.replyUserId").description("대댓글의 작성자 유저 Id"),
                                        fieldWithPath("data.replyUserName").description("대댓글 작성자 유저 이름"),
                                        fieldWithPath("data.content").description("대댓글 내용"),
                                        fieldWithPath("data.createdDate").description("대댓글 생성 날짜")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardReplySaveRespDto boardReplySaveRespDto = objectMapper.treeToValue(jsonNode, BoardReplySaveRespDto.class);

        assertThat(boardReplySaveRespDto.getTopCommentId()).isEqualTo(boardId);
        assertThat(boardReplySaveRespDto.getReplyUserId()).isEqualTo(user.getId());
        assertThat(boardReplySaveRespDto.getReplyUserName()).isEqualTo(user.getUsername());
        assertThat(boardReplySaveRespDto.getContent()).isEqualTo(content);
    }
}