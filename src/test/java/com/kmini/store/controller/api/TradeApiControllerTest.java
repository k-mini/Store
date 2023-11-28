package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.config.WithMockCustomUserSecurityContextFactory;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.response.TradeDto.TradeRegisterRespDto;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.impl.ItemBoardServiceImpl;
import com.kmini.store.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Slf4j
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class TradeApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ItemBoardServiceImpl itemBoardService;
    @Autowired
    EntityManager em;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WithMockCustomUserSecurityContextFactory securityContextFactory;

    @TestConfiguration
    static class TestConfig {
        @Autowired
        UserRepository userRepository;
        @Bean
        public WithMockCustomUserSecurityContextFactory securityContextFactory() {
            return new WithMockCustomUserSecurityContextFactory(userRepository);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // 테스트를 위한 게시물 작성
        String category = "trade";
        String subCategory = "electronics";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream("C:\\Users\\kmin\\images\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardFormSaveDto formSaveDto = new ItemBoardFormSaveDto(subCategory, "Life is Good", "what is your favorite food?", existingFile, null);
        ItemBoard itemBoard = itemBoardService.save(formSaveDto);

        em.clear();
    }

    @WithMockCustomUser
    @DisplayName("거래 신청")
    @Test
    void registerTrade() throws Exception {

        // given
        User user1 = ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 유저 1로 게시물 등록했으니 유저 2로 변경
        SecurityContext securityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        User user2 = ((AccountContext) securityContext.getAuthentication().getPrincipal()).getUser();

        String boardId = "1";
        String buyerId = String.valueOf(user2.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buyerId", buyerId);
        String data = objectMapper.writeValueAsString(jsonObject.toString());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/trade/" + boardId)
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.securityContext(securityContext))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
                                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                           .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(result).get("data");

        TradeRegisterRespDto tradeRegisterRespDto = objectMapper.treeToValue(jsonNode, TradeRegisterRespDto.class);
        assertThat(tradeRegisterRespDto.getSellerId()).isEqualTo(user1.getId());
        assertThat(tradeRegisterRespDto.getSellerName()).isEqualTo(user1.getUsername());
        assertThat(tradeRegisterRespDto.getBuyerId()).isEqualTo(user2.getId());
        assertThat(tradeRegisterRespDto.getBuyerName()).isEqualTo(user2.getUsername());
    }

    @Test
    void acceptTrade() {
    }

    @Test
    void denyTrade() {
    }

    @Test
    void completeTrade() {
    }

    @Test
    void cancelTrade() {
    }
}