package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.TestDataProvider;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.BoardDto.ItemBoardSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.response.CommentDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentSaveRespDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentSelectRespDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentUpdateRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardSaveRespDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Autowired
    TestDataProvider testDataProvider;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트를 위한 게시물 작성 (유저 1이 작성)
        String category = "trade";
        String subCategory = "electronics";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardSaveReqDto itemBoardSaveReqDto = new ItemBoardSaveReqDto("Life is Good", "what is your favorite food?", existingFile, null);
        ItemBoard itemBoard = ItemBoard.builder()
                .title(itemBoardSaveReqDto.getTitle())
                .content(itemBoardSaveReqDto.getContent())
                .file(itemBoardSaveReqDto.getFile())
                .itemName(itemBoardSaveReqDto.getItemName())
                .subCategoryName(subCategory)
                .build();
        ItemBoardSaveRespDto itemBoardSaveRespDto = itemBoardService.saveBoard(itemBoard);
        log.info("itemBoardSaveRespDto id = {}", itemBoardSaveRespDto.getId());
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

        BoardCommentSaveReqDto boardCommentSaveReqDto = new BoardCommentSaveReqDto(boardId, null, content);
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
                                getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("boardId").description("작성할 댓글의 게시판 ID"),
                                        fieldWithPath("topCommentId").description("새로 작성할 댓글의 부모 댓글 ID").optional(),
                                        fieldWithPath("content").description("새로 작성할 댓글 내용")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("새로 작성된 댓글 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("댓글 ID"),
                                        fieldWithPath("topCommentId").description("부모 댓글 ID"),
                                        fieldWithPath("commentUserId").description("작성자 Id"),
                                        fieldWithPath("commentUserName").description("작성자 이름"),
                                        fieldWithPath("content").description("댓글 내용"),
                                        fieldWithPath("replies").description("하위 댓글"),
                                        fieldWithPath("createdDate").description("생성 날짜")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description(APPLICATION_JSON)
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardCommentSaveRespDto boardCommentSaveRespDto = objectMapper.treeToValue(jsonNode, BoardCommentSaveRespDto.class);

        assertThat(boardCommentSaveRespDto.getTopCommentId()).isEqualTo(boardCommentSaveReqDto.getTopCommentId());
        assertThat(boardCommentSaveRespDto.getCommentUserId()).isEqualTo(user.getId());
        assertThat(boardCommentSaveRespDto.getCommentUserName()).isEqualTo(user.getUsername());
        assertThat(boardCommentSaveRespDto.getContent()).isEqualTo(content);
    }

    @WithMockCustomUser
    @DisplayName("댓글 조회 (특정 게시물)")
    @Test
    void selectComments() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String title = "Life is Good";
        String content = "what is your favorite food?";
        ItemBoardSaveRespDto itemBoardSaveRespDto = testDataProvider.createTestItemBoard(category, title, content, null);
        Long boardId = itemBoardSaveRespDto.getId();

        String topCommentContent = "부모 댓글 내용입니다.";
        String subCommentContent = "자식 댓글 내용입니다.";

        BoardCommentSaveRespDto topComment = testDataProvider.createTestComment(boardId, null, topCommentContent);
        BoardCommentSaveRespDto subComment = testDataProvider.createTestComment(boardId, topComment.getId(), subCommentContent);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/comment/{boardId}", boardId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                        "comment/select-comment",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("댓글들을 조회하고자 하는 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                fieldWithPath("message").description("응답 관련 메시지"),
                                subsectionWithPath("data").description("조회된 댓글 JSON")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("id").description("댓글 ID"),
                                fieldWithPath("topCommentId").description("상위 댓글 ID"),
                                fieldWithPath("commentUserId").description("댓글 작성 유저 ID"),
                                fieldWithPath("commentUserName").description("댓글 작성 유저명"),
                                fieldWithPath("content").description("댓글 내용"),
                                subsectionWithPath("replies").description("하위 댓글 JSON"),
                                fieldWithPath("createdDate").description("댓글 작성 날짜")
                        ),
                        responseFields(
                                beneathPath("data.[].replies").withSubsectionId("replies"),
                                fieldWithPath("[].id").description("대댓글 ID"),
                                fieldWithPath("[].topCommentId").description("상위 댓글 ID"),
                                fieldWithPath("[].commentUserId").description("대댓글 작성 유저 ID"),
                                fieldWithPath("[].commentUserName").description("대댓글 작성 유저명"),
                                fieldWithPath("[].content").description("대댓글 내용"),
                                subsectionWithPath("[].replies").description("대 댓글 JSON(계층형이 아니기 때문에 미구현)"),
                                fieldWithPath("[].createdDate").description("대댓글 작성 날짜")
                        )
                    )
                ).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result);
        List<BoardCommentSelectRespDto> boardCommentSelectRespDtos =
                Arrays.asList(objectMapper.treeToValue(jsonNode.get("data"), BoardCommentSelectRespDto[].class));


        // 댓글 검증
        for (BoardCommentSelectRespDto topCommentDto : boardCommentSelectRespDtos) {
            assertThat(topCommentDto.getId()).isEqualTo(topComment.getId());
            assertThat(topCommentDto.getTopCommentId()).isNull();
            assertThat(topCommentDto.getCommentUserId()).isEqualTo(topComment.getCommentUserId());
            assertThat(topCommentDto.getCommentUserName()).isEqualTo(topComment.getCommentUserName());
            assertThat(topCommentDto.getContent()).isEqualTo(topComment.getContent());
            assertThat(topCommentDto.getReplies()).hasSize(1);

            for (BoardCommentSelectRespDto subCommentDto : topCommentDto.getReplies()) {
                assertThat(subCommentDto.getId()).isEqualTo(subComment.getId());
                assertThat(subCommentDto.getTopCommentId()).isEqualTo(subComment.getTopCommentId());
                assertThat(subCommentDto.getCommentUserId()).isEqualTo(subComment.getCommentUserId());
                assertThat(subCommentDto.getCommentUserName()).isEqualTo(subComment.getCommentUserName());
                assertThat(subCommentDto.getContent()).isEqualTo(subComment.getContent());
                assertThat(subCommentDto.getReplies()).hasSize(0);
            }
        }
    }

    @WithMockCustomUser
    @DisplayName("댓글 수정")
    @Test
    void updateComment() throws Exception {
        // given
        User user = User.getSecurityContextUser();
        String commentContent = "댓글 내용내용";
        BoardCommentSaveRespDto commentSaveRespDto = testDataProvider.createTestComment(1L, null, commentContent);
        em.clear();

        Long commentId = commentSaveRespDto.getId();
        String content = "수정내용내용";

        BoardCommentUpdateReqDto boardCommentUpdateReqDto = new BoardCommentUpdateReqDto(content);
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
                                        fieldWithPath("content").description("수정할 댓글 내용")
                                ),
                                responseFields(
//                                        Attributes.attributes(key("title").value("example for title")),
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("수정된 댓글 JSON")
                                ),
                                responseFields(
                                        // withSubsectionId : 스니펫 이름 지정
                                        // beneathPath 경로 아래를 명세한다.
                                        beneathPath("data"),
//                                        Attributes.attributes(key("title").value("example for title")),
                                        fieldWithPath("id").description("수정된 댓글의 Id"), //.attributes(key("etc").value("this is etc")),
                                        fieldWithPath("topCommentId").description("부모 댓글 ID"),
                                        fieldWithPath("commentUserId").description("수정된 댓글의 작성 유저 Id"),
                                        fieldWithPath("commentUserName").description("수정된 댓글의 작성 유저 이름"),
                                        fieldWithPath("content").description("수정된 댓글 내용"),
                                        fieldWithPath("replies").description("수정된 댓글의 대댓글 배열"),
                                        fieldWithPath("createdDate").description("수정된 시간")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description(APPLICATION_JSON)
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
        String commentContent = "댓글 내용내용";
        BoardCommentSaveRespDto commentSaveRespDto = testDataProvider.createTestComment(1L, null, commentContent);
        em.clear();

        Long commentId = commentSaveRespDto.getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/comment/{commentId}", commentId)
        );

        //then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document(
                                "comment/delete-comment",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("commentId").description("삭제할 댓글의 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data").description("삭제된 댓글 수")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String code = objectMapper.readTree(result).get("code").asText();
        String message = objectMapper.readTree(result).get("message").asText();
        String data = objectMapper.readTree(result).get("data").asText();

        assertThat(code).isEqualTo("1");
        assertThat(message).isEqualTo("성공");
        assertThat(data).isEqualTo("1");
    }
}