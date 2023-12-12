package com.kmini.store.controller;

import com.kmini.store.config.WithMockCustomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockCustomUser
    @DisplayName("거래 게시판 목록 화면 불러오기 (로그인 상태)")
    @Test
    void viewPostsWithTrade() throws Exception {
        // given
        String category = "trade";
        String subCategory = "all";
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/boards/{category}/{subCategory}",category, subCategory)
        );
        // then
        resultActions.andExpect(status().isOk())
                     .andExpect(view().name("board/boardList"));
    }

    @WithMockCustomUser
    @DisplayName("커뮤니티 게시판 목록 화면 불러오기 (로그인 상태)")
    @Test
    void viewPostsWithCommunity() throws Exception {
        // given
        String category = "community";
        String subCategory = "all";
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/boards/{category}/{subCategory}", category, subCategory)
        );
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(view().name("board/boardList"));
    }


}