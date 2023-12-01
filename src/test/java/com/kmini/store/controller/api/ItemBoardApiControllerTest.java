package com.kmini.store.controller.api;

import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
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

        // when
        ResultActions resultActions = mockMvc.perform(
                delete( "/api/board/" + category + "/"  + subCategory + "/" + itemBoard.getId())
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);

        // when
        ResultActions resultActions = mockMvc.perform(
                multipart(HttpMethod.PATCH, "/api/board/" + category + "/"  + subCategory + "/" + itemBoard.getId())
                        .file(file)
                        .param("title", formSaveDto.getTitle() + "(Modified)")
                        .param("content", formSaveDto.getContent() + "(Modified)")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}