package com.kmini.store.controller;

import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.domain.entity.ItemBoard;
import com.kmini.store.domain.board.item.dto.ItemBoardRequestDto.ItemBoardSaveReqDto;
import com.kmini.store.domain.board.item.dto.ItemBoardReponseDto.ItemBoardSaveRespDto;
import com.kmini.store.domain.board.item.service.ItemBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ItemBoardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ItemBoardService itemBoardService;
    @Autowired
    EntityManager em;

    @BeforeEach
    public void setUp() throws ServletException {
//        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
//        delegatingFilterProxy.init(new MockFilterConfig(applicationContext.getServletContext(), "filterChainProxy"));
//        mockMvc = MockMvcBuilders
//                    .webAppContextSetup(applicationContext)
//                    .addFilter(delegatingFilterProxy).build();

//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(applicationContext)
//                .apply(springSecurity(filterChain)).build();
    }

    @WithMockCustomUser
    @DisplayName("게시물 상세화면 진입")
    @Test
    void viewBoard() throws Exception {
        // given
        String category = "trade";
        String subCategory = "electronics";
        ItemBoardSaveReqDto itemBoardSaveReqDto = new ItemBoardSaveReqDto("Life is Good", "what is your favorite food?", null, null, null, null);
        ItemBoard itemBoard = ItemBoard.builder()
                .title(itemBoardSaveReqDto.getTitle())
                .content(itemBoardSaveReqDto.getContent())
                .file(itemBoardSaveReqDto.getFile())
                .itemName(itemBoardSaveReqDto.getItemName())
                .subCategoryName(subCategory)
                .build();
        ItemBoardSaveRespDto itemBoardSaveRespDto = itemBoardService.saveBoard(itemBoard);
        em.clear();
        Long boardId = itemBoardSaveRespDto.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/board/{category}/{subCategory}/{boardId}", category, subCategory, boardId)
            );

        // then
        resultActions.andExpect(view().name("board/tradedetail"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockCustomUser
    @Transactional
    @DisplayName("거래 게시판 수정화면 진입")
    @Test
    void getUpdateForm() throws Exception {

        // given
        String category = "trade";
        String subCategory = "electronics";
        ItemBoardSaveReqDto itemBoardSaveReqDto = new ItemBoardSaveReqDto("Life is Good", "what is your favorite food?", null, null, null, null);
        ItemBoard itemBoard = ItemBoard.builder()
                                        .title(itemBoardSaveReqDto.getTitle())
                                        .content(itemBoardSaveReqDto.getContent())
                                        .file(itemBoardSaveReqDto.getFile())
                                        .itemName(itemBoardSaveReqDto.getItemName())
                                        .subCategoryName(subCategory)
                                        .build();
        ItemBoardSaveRespDto itemBoardSaveRespDto = itemBoardService.saveBoard(itemBoard);
        em.clear();
        Long boardId = itemBoardSaveRespDto.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/board/{category}/{subCategory}/{boardId}/form", category, subCategory, boardId)
        );

        // then
        resultActions.andExpect(view().name("board/updateform"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 등록 화면 진입")
    @Test
    void getSaveForm() throws Exception {

        // given
        String category = "trade";
        String subCategory = "electronics";

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/board/{category}/{subCategory}/form",category, subCategory)
        );

        // then
        resultActions.andExpect(view().name("board/form"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockCustomUser
    @DisplayName("거래 게시판 게시물 등록")
    @Test
    void saveBoard() throws Exception {

        // given
        String category = "trade";
        String subCategory = "electronics";
        String title = "이 값은 제목입니다.";
        String content = "콘텐츠 내용입니다.";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile file = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);

        // when
        ResultActions resultActions = mockMvc.perform(
                multipart("/board/{category}/{subCategory}/form",category, subCategory)
                        .file(file)
                        .param("title", title)
                        .param("content",content)
        );

        // then
        resultActions
                .andExpect(redirectedUrl("/boards/" + category+ "/" + subCategory))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}