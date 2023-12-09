package com.kmini.store.controller.api;

import com.kmini.store.config.ApiDocumentUtils;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.FileInputStream;

import static com.kmini.store.config.ApiDocumentUtils.getDocumentRequest;
import static com.kmini.store.config.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class ItemBoardApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    EntityManager em;
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
        em.clear();

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
                                "delete-itemboard",
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
        em.clear();

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
                        .param("title", formSaveDto.getTitle() + "(Modified)")
                        .param("content", formSaveDto.getContent() + "(Modified)")
                        .with(request -> {
                            request.setMethod(HttpMethod.PATCH.toString());
                            return request;
                        })
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(
                        "update-itemboard",
                        getDocumentRequest(),getDocumentResponse(),
                        pathParameters(
                                parameterWithName("category").description("카테고리 이름"),
                                parameterWithName("subCategory").description("소 카테고리 이름"),
                                parameterWithName("boardId").description("게시물 Id")
                        ),
                        requestParts(
                                partWithName("file").optional().description("게시판의 수정할 사진")
                        ),
                        requestParameters(
                                parameterWithName("title").description("게시물 제목"),
                                parameterWithName("content").description("게시물 내용")
                        )
                    )
                );
    }


}