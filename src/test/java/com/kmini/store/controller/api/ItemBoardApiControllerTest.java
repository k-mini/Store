package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.request.BoardDto.ItemBoardSaveReqDto;
import com.kmini.store.dto.request.CommentDto;
import com.kmini.store.dto.request.ItemBoardDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateReqApiDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentSaveRespDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentUpdateRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardDeleteRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardSaveRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardUpdateRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;
import com.kmini.store.service.CommentService;
import com.kmini.store.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class ItemBoardApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    EntityManager entityManager;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ItemBoardService itemBoardService;
    @Autowired
    CommentService commentService;

    public ItemBoardSaveRespDto createTestItemBoard(String subCategory, String title, String content, String itemName) throws Exception {
        String category = "trade";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardSaveReqDto itemBoardSaveReqDto = new ItemBoardSaveReqDto(title, content, existingFile, itemName);
        ItemBoard itemBoard = ItemBoard.builder()
                .title(itemBoardSaveReqDto.getTitle())
                .content(itemBoardSaveReqDto.getContent())
                .file(itemBoardSaveReqDto.getFile())
                .itemName(itemBoardSaveReqDto.getItemName())
                .build();
        ItemBoardSaveRespDto itemBoardSaveRespDto = itemBoardService.saveBoard(itemBoard, subCategory);
        entityManager.clear();
        return itemBoardSaveRespDto;
    }

    private BoardCommentSaveRespDto createTestComment(Long boardId, Long topCommentId, String commentContent) {
        BoardCommentSaveRespDto boardCommentSaveRespDto = commentService.saveComment(new CommentDto.BoardCommentSaveReqDto(boardId, topCommentId, commentContent));
        entityManager.clear();
        return boardCommentSaveRespDto;
    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 조회")
    @Test
    void viewBoard() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String title = "Life is Good";
        String content = "what is your favorite food?";
        ItemBoardSaveRespDto itemBoardSaveRespDto = createTestItemBoard(category, title, content, null);
        Long boardId = itemBoardSaveRespDto.getId();
        String topCommentContent = "부모 댓글 내용입니다.";
        String subCommentContent = "자식 댓글 내용입니다.";

        BoardCommentSaveRespDto topComment = createTestComment(boardId, null, topCommentContent);
        BoardCommentSaveRespDto subComment = createTestComment(boardId, topComment.getId(), subCommentContent);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                                "itemboard/select-itemboard",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("category").description("카테고리 이름"),
                                        parameterWithName("subCategory").description("소 카테고리 이름"),
                                        parameterWithName("boardId").description("게시물 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        subsectionWithPath("data").description("응답 데이터 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("조회된 게시물 ID"),
                                        fieldWithPath("email").description("조회된 게시물의 작성자 이메일"),
                                        fieldWithPath("writerId").description("조회된 게시물의 유저 ID"),
                                        fieldWithPath("username").description("조회된 게시물의 유저명"),
                                        fieldWithPath("userThumbnail").description("조회된 게시물의 유저 사진 URI"),
                                        fieldWithPath("title").description("조회된 게시물의 제목"),
                                        fieldWithPath("boardThumbnail").description("조회된 게시물 사진 URI"),
                                        fieldWithPath("content").description("조회된 게시물의 내용"),
                                        fieldWithPath("createdDate").description("조회된 게시물의 생성 날짜"),
                                        fieldWithPath("views").description("조회된 게시물의 조회수"),
                                        fieldWithPath("commentTotalCount").description("조회된 게시물 댓글 개수"),
                                        subsectionWithPath("comments").description("조회된 게시물의 댓글 JSON"),
                                        fieldWithPath("tradePossible").description("조회된 게시물의 거래 가능 여부")
                                ),
                                responseFields(
                                        beneathPath("data.comments"),
                                        fieldWithPath("id").description("댓글 ID"),
                                        fieldWithPath("topCommentId").description("상위 댓글 ID"),
                                        fieldWithPath("commentUserId").description("댓글 작성 유저 ID"),
                                        fieldWithPath("commentUserName").description("댓글 작성 유저명"),
                                        fieldWithPath("content").description("댓글 내용"),
                                        subsectionWithPath("replies").description("하위 댓글 JSON"),
                                        fieldWithPath("createdDate").description("댓글 작성 날짜")
                                ),
                                responseFields(
                                        beneathPath("data.comments[].replies").withSubsectionId("replies"),
                                        fieldWithPath("[].id").description("대댓글 ID"),
                                        fieldWithPath("[].topCommentId").description("상위 댓글 ID"),
                                        fieldWithPath("[].commentUserId").description("대댓글 작성 유저 ID"),
                                        fieldWithPath("[].commentUserName").description("대댓글 작성 유저명"),
                                        fieldWithPath("[].content").description("대댓글 내용"),
                                        subsectionWithPath("[].replies").description("대 댓글 JSON(계층형이 아니기 때문에 미구현)"),
                                        fieldWithPath("[].createdDate").description("대댓글 작성 날짜")
                                )
                                // 해당 방법은 안됨 issue 같음
//                                responseFields(
//                                        beneathPath("data.comments[].replies.[]"),
//                                        fieldWithPath("id").description("대댓글 ID"),
//                                        fieldWithPath("topCommentId").description("상위 댓글 ID"),
//                                        fieldWithPath("commentUserId").description("대댓글 작성 유저 ID"),
//                                        fieldWithPath("commentUserName").description("대댓글 작성 유저명"),
//                                        fieldWithPath("content").description("대댓글 내용"),
//                                        subsectionWithPath("replies").description("대 댓글 JSON(계층형이 아니기 때문에 미구현)"),
//                                        fieldWithPath("createdDate").description("대댓글 작성 날짜")
//                                )
                        )
                ).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result);
        String code = jsonNode.get("code").asText();
        String message = jsonNode.get("message").asText();

        assertThat(code).isEqualTo("1");
        assertThat(message).isEqualTo("성공");

        ItemBoardViewRespDto itemBoardViewRespDto = objectMapper.treeToValue(jsonNode.get("data"), ItemBoardViewRespDto.class);

        // 게시판 내용
        assertThat(itemBoardViewRespDto.getId()).isEqualTo(itemBoardSaveRespDto.getId());
        assertThat(itemBoardViewRespDto.getEmail()).isEqualTo(itemBoardSaveRespDto.getEmail());
        assertThat(itemBoardViewRespDto.getWriterId()).isEqualTo(itemBoardSaveRespDto.getWriterId());
        assertThat(itemBoardViewRespDto.getUsername()).isEqualTo(itemBoardSaveRespDto.getUsername());
        assertThat(itemBoardViewRespDto.getUserThumbnail()).isEqualTo(itemBoardSaveRespDto.getUserThumbnail());
        assertThat(itemBoardViewRespDto.getTitle()).isEqualTo(itemBoardSaveRespDto.getTitle());
        assertThat(itemBoardViewRespDto.getBoardThumbnail()).isEqualTo(itemBoardSaveRespDto.getBoardThumbnail());
        assertThat(itemBoardViewRespDto.getContent()).isEqualTo(itemBoardSaveRespDto.getContent());
        assertThat(itemBoardViewRespDto.getCreatedDate()).isEqualTo(itemBoardSaveRespDto.getCreatedDate());
        assertThat(itemBoardViewRespDto.getViews()).isEqualTo(1);
        assertThat(itemBoardViewRespDto.getCommentTotalCount()).isEqualTo(2);
        assertThat(itemBoardViewRespDto.isTradePossible()).isTrue();

        // 댓글 내용
        List<BoardCommentUpdateRespDto> comments = itemBoardViewRespDto.getComments();

        BoardCommentUpdateRespDto comment = comments.get(0);
        assertThat(comment.getId()).isEqualTo(topComment.getId());
        assertThat(comment.getTopCommentId()).isNull();
        assertThat(comment.getCommentUserId()).isEqualTo(topComment.getCommentUserId());
        assertThat(comment.getCommentUserName()).isEqualTo(topComment.getCommentUserName());
        assertThat(comment.getContent()).isEqualTo(topComment.getContent());
        assertThat(comment.getReplies()).hasSize(1);

        BoardCommentUpdateRespDto replyComment = comment.getReplies().get(0);
        assertThat(replyComment.getId()).isEqualTo(subComment.getId());
        assertThat(replyComment.getTopCommentId()).isEqualTo(subComment.getTopCommentId());
        assertThat(replyComment.getCommentUserId()).isEqualTo(subComment.getCommentUserId());
        assertThat(replyComment.getCommentUserName()).isEqualTo(subComment.getCommentUserName());
        assertThat(replyComment.getContent()).isEqualTo(subComment.getContent());
        assertThat(replyComment.getReplies()).hasSize(0);

    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 삭제")
    @Test
    void deletePost() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String title = "Life is Good";
        String content = "what is your favorite food?";
        ItemBoardSaveRespDto itemBoardSaveRespDto = createTestItemBoard(category, title, content, null);

        Long boardId = itemBoardSaveRespDto.getId();
        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                                "itemboard/delete-itemboard",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("category").description("카테고리 이름"),
                                        parameterWithName("subCategory").description("소 카테고리 이름"),
                                        parameterWithName("boardId").description("게시물 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        subsectionWithPath("data").description("응답 데이터 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("삭제된 게시물 ID"),
                                        fieldWithPath("email").description("삭제된 게시물의 작성자 이메일"),
                                        fieldWithPath("writerId").description("삭제된 게시물의 유저 ID"),
                                        fieldWithPath("username").description("삭제된 게시물의 유저명"),
                                        fieldWithPath("userThumbnail").description("삭제된 게시물의 유저 사진 URI"),
                                        fieldWithPath("title").description("삭제된 게시물의 제목"),
                                        fieldWithPath("boardThumbnail").description("삭제된 게시물 사진 URI"),
                                        fieldWithPath("content").description("삭제된 게시물의 내용"),
                                        fieldWithPath("createdDate").description("삭제된 게시물의 생성 날짜")
                                )
                        )
                ).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        ItemBoardDeleteRespDto itemBoardDeleteRespDto = objectMapper.treeToValue(jsonNode, ItemBoardDeleteRespDto.class);

        assertThat(itemBoardDeleteRespDto.getId()).isEqualTo(boardId);
        assertThat(itemBoardDeleteRespDto.getEmail()).isEqualTo(itemBoardSaveRespDto.getEmail());
        assertThat(itemBoardDeleteRespDto.getWriterId()).isEqualTo(itemBoardSaveRespDto.getWriterId());
        assertThat(itemBoardDeleteRespDto.getUsername()).isEqualTo(itemBoardSaveRespDto.getUsername());
        assertThat(itemBoardDeleteRespDto.getUserThumbnail()).isEqualTo(itemBoardSaveRespDto.getUserThumbnail());
        assertThat(itemBoardDeleteRespDto.getTitle()).isEqualTo(itemBoardSaveRespDto.getTitle());
        assertThat(itemBoardDeleteRespDto.getBoardThumbnail()).isEqualTo(itemBoardSaveRespDto.getBoardThumbnail());
        assertThat(itemBoardDeleteRespDto.getContent()).isEqualTo(itemBoardSaveRespDto.getContent());
        assertThat(itemBoardDeleteRespDto.getCreatedDate()).isEqualTo(itemBoardSaveRespDto.getCreatedDate());
    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 수정")
    @Test
    void updatePost() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String title = "Life is Good";
        String content = "what is your favorite food?";
        ItemBoardSaveRespDto itemBoardSaveRespDto = createTestItemBoard(category, title, content, null);

        // 수정 내용 작성
        String itemName = "book";
        String fileName = "testImage2";
        String contentType = "jpg";
        String modifiedTitle = itemBoardSaveRespDto.getTitle() + "(Modified)";
        String modifiedContent = itemBoardSaveRespDto.getContent() + "(Modified)";

        ItemBoardUpdateReqApiDto itemBoardUpdateReqApiDto = ItemBoardUpdateReqApiDto.builder()
                .itemName(itemName)
                .title(modifiedTitle)
                .content(modifiedContent).build();
        FileInputStream inputStream = new FileInputStream("C:\\Users\\kmin\\images\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, MediaType.IMAGE_JPEG_VALUE, inputStream);
        Long boardId = itemBoardSaveRespDto.getId();

        // part 생성
        String itemBoardUpdateReqApiDtoAsString = objectMapper.writeValueAsString(itemBoardUpdateReqApiDto);
        MockPart jsonPart = new MockPart("itemBoardUpdateReqApiDto", itemBoardUpdateReqApiDtoAsString.getBytes(StandardCharsets.UTF_8));
        jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        MockPart filePart = new MockPart("file", file.getOriginalFilename(), file.getBytes());
        filePart.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
                        .part(filePart, jsonPart)
                        .with(request -> {
                            request.setMethod(HttpMethod.PATCH.toString());
                            return request;
                        })
        );

        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                                "itemboard/update-itemboard",
                                getDocumentRequest(), getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("category").description("게시물이 속한 카테고리명"),
                                        parameterWithName("subCategory").description("게시물이 속한 소 카테고리명"),
                                        parameterWithName("boardId").description("수정할 게시물 ID")
                                ),
                                requestParts(
                                        partWithName("file").description("수정할 게시판의 사진").optional(),
                                        partWithName("itemBoardUpdateReqApiDto").description("수정할 게시물 관련 JSON")
                                ),
                                requestPartFields(
                                        "itemBoardUpdateReqApiDto",
                                        fieldWithPath("boardId").description("수정할 게시물 ID"),
                                        fieldWithPath("itemName").description("수정할 게시물의 아이템명").optional(),
                                        fieldWithPath("title").description("수정할 게시물 제목"),
                                        fieldWithPath("content").description("수정할 게시물 내용"),
                                        fieldWithPath("subCategory").description("수정할 게시물 하위 카테고리")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        subsectionWithPath("data").description("수정된 게시물 관련 JSON")
                                ),
                                responseFields(
                                        beneathPath("data"),
                                        fieldWithPath("id").description("수정된 게시물 ID"),
                                        fieldWithPath("itemName").description("수정된 게시물의 아이템명"),
                                        fieldWithPath("email").description("수정된 게시물의 작성자 이메일"),
                                        fieldWithPath("writerId").description("수정된 게시물의 유저 ID"),
                                        fieldWithPath("username").description("수정된 게시물의 유저명"),
                                        fieldWithPath("title").description("수정된 게시물의 제목"),
                                        fieldWithPath("content").description("수정된 게시물의 내용"),
                                        fieldWithPath("createdDate").description("수정된 게시물의 생성 날짜")
                                )
                        )
                ).andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result);
        ItemBoardUpdateRespDto itemBoardUpdateRespDto = objectMapper.treeToValue(jsonNode.get("data"), ItemBoardUpdateRespDto.class);

        assertThat(itemBoardUpdateReqApiDto.getItemName()).isEqualTo(itemBoardUpdateRespDto.getItemName());
        assertThat(itemBoardUpdateReqApiDto.getTitle()).isEqualTo(itemBoardUpdateRespDto.getTitle());
        assertThat(itemBoardUpdateReqApiDto.getContent()).isEqualTo(itemBoardUpdateRespDto.getContent());

    }


}