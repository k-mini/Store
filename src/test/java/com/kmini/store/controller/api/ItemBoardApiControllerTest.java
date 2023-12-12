package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.dto.response.ItemBoardDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardUpdateRespDto;
import com.kmini.store.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 삭제")
    @Test
    void deletePost() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardFormSaveDto formSaveDto = new ItemBoardFormSaveDto(subCategory, "Life is Good", "what is your favorite food?", existingFile, null);
        ItemBoard itemBoard = itemBoardService.save(formSaveDto);
        entityManager.clear();

        Long boardId = itemBoard.getId();
        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
        );

        // then
        resultActions.andExpect(status().isOk())
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
                                        fieldWithPath("data").description("응답 데이터")
                                )
                        )
                );
    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 수정")
    @Test
    void updatePost() throws Exception {

        // given
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        FileInputStream existingContentStream = new FileInputStream("C:\\Users\\kmin\\images\\test\\testImage.png");
        MockMultipartFile existingFile = new MockMultipartFile("file", "testImage.png", "png", existingContentStream);
        ItemBoardFormSaveDto formSaveDto = new ItemBoardFormSaveDto(subCategory, "Life is Good", "what is your favorite food?", existingFile, null);
        ItemBoard itemBoard = itemBoardService.save(formSaveDto);
        entityManager.clear();

        // 수정 내용 작성
        String fileName = "testImage2";
        String contentType = "jpg";
        ItemBoardUpdateFormDto updateFormDto = ItemBoardUpdateFormDto.builder()
                .title(formSaveDto.getTitle() + "(Modified)")
                .content(formSaveDto.getContent() + "(Modified)").build();
        FileInputStream inputStream = new FileInputStream("C:\\Users\\kmin\\images\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, MediaType.IMAGE_JPEG_VALUE, inputStream);
        Long boardId = itemBoard.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
                        .file(file)
                        .param("title", updateFormDto.getTitle())
                        .param("content", updateFormDto.getContent())
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
                                requestParameters(
                                        parameterWithName("file").description("수정할 게시판의 사진").optional(),
                                        parameterWithName("title").description("수정할 게시물 제목"),
                                        parameterWithName("content").description("수정할 게시물 내용")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("성공 코드 (성공 : 1, 실패 :0)"),
                                        fieldWithPath("message").description("응답 관련 메시지"),
                                        fieldWithPath("data.id").description("수정된 게시물 ID"),
                                        fieldWithPath("data.email").description("수정된 게시물의 작성자 이메일"),
                                        fieldWithPath("data.writerId").description("수정된 게시물의 유저 ID"),
                                        fieldWithPath("data.username").description("수정된 게시물의 유저명"),
                                        fieldWithPath("data.title").description("수정된 게시물의 제목"),
                                        fieldWithPath("data.content").description("수정된 게시물의 내용"),
                                        fieldWithPath("data.createdDate").description("수정된 게시물의 생성 날짜")
                                )
                        )
                )
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result);
        ItemBoardUpdateRespDto itemBoardUpdateRespDto = objectMapper.treeToValue(jsonNode.get("data"), ItemBoardUpdateRespDto.class);

        assertThat(updateFormDto.getTitle()).isEqualTo(itemBoardUpdateRespDto.getTitle());
        assertThat(updateFormDto.getContent()).isEqualTo(itemBoardUpdateRespDto.getContent());

    }


}