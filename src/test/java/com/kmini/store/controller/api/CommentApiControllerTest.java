package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.BoardDto;
import com.kmini.store.dto.request.CommentDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplyRespDto;
import com.kmini.store.service.CommentService;
import com.kmini.store.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:/tableInit.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
@Transactional
@SpringBootTest
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
        BoardDto.ItemBoardFormSaveDto formSaveDto = new BoardDto.ItemBoardFormSaveDto(subCategory, "Life is Good", "what is your favorite food?", existingFile, null);
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
        String content = "댓글내용";

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
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardCommentRespDto boardCommentDto = objectMapper.treeToValue(jsonNode, BoardCommentRespDto.class);
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
        BoardCommentRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();
        Long boardId = 1L;
        String content = "수정내용내용";

        BoardCommentUpdateReqDto boardCommentUpdateReqDto = new BoardCommentUpdateReqDto(boardId, commentId, content);
        String requestBody = objectMapper.writeValueAsString(boardCommentUpdateReqDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/comment/" + commentId)
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
        );

        //then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardCommentRespDto boardCommentDto = objectMapper.treeToValue(jsonNode, BoardCommentRespDto.class);
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
        BoardCommentRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/comment/" + commentId)
        );

        //then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
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
        BoardCommentRespDto commentSaveResult = commentService.saveComment(new BoardCommentSaveReqDto(1L, "댓글 내용내용"));
        Long commentId = commentSaveResult.getId();
        Long boardId = 1L;
        String content = "대댓글내용";

        BoardReplySaveDto boardReplySaveDto = new BoardReplySaveDto(boardId, commentId, content);
        String requestBody = objectMapper.writeValueAsString(boardReplySaveDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/comment/" + commentId + "/reply")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON)
        );

        //then
        String result = resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        BoardReplyRespDto boardReplyRespDto = objectMapper.treeToValue(jsonNode, BoardReplyRespDto.class);
        assertThat(boardReplyRespDto.getTopCommentId()).isEqualTo(boardId);
        assertThat(boardReplyRespDto.getReplyUserId()).isEqualTo(user.getId());
        assertThat(boardReplyRespDto.getReplyUserName()).isEqualTo(user.getUsername());
        assertThat(boardReplyRespDto.getContent()).isEqualTo(content);
    }
}