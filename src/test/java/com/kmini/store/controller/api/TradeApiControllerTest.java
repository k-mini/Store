package com.kmini.store.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.config.WithMockCustomUser;
import com.kmini.store.config.WithMockCustomUserSecurityContextFactory;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.response.TradeDto.*;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.ItemBoardService;
import com.kmini.store.service.TradeService;
import com.kmini.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;

import static com.kmini.store.domain.type.CompleteFlag.COMPLETE_ABSTAIN;
import static com.kmini.store.domain.type.CompleteFlag.COMPLETE_CONFIRM;
import static com.kmini.store.domain.type.TradeStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql("classpath:/tableInit.sql")
@ActiveProfiles("test")
@Slf4j
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class TradeApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    ItemBoardService itemBoardService;
    @Autowired
    TradeService tradeService;
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
    @DisplayName("거래 신청")
    @Test
    void registerTrade() throws Exception {
        // given
        User seller = ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 판매자로 게시물 등록했으니 구매자로 변경
        SecurityContext buyerSecurityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        User buyer = ((AccountContext) buyerSecurityContext.getAuthentication().getPrincipal()).getUser();

        String boardId = "1";
        String buyerId = String.valueOf(buyer.getId());
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("buyerId", buyerId);
//        String data = objectMapper.writeValueAsString(jsonObject.toString());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/trade/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(securityContext(buyerSecurityContext))
        );

        // then
        String result = resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(result).get("data");

        TradeRegisterRespDto tradeRegisterRespDto = objectMapper.treeToValue(jsonNode, TradeRegisterRespDto.class);
        assertThat(tradeRegisterRespDto.getSellerId()).isEqualTo(seller.getId());
        assertThat(tradeRegisterRespDto.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(tradeRegisterRespDto.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(tradeRegisterRespDto.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(tradeRegisterRespDto.getTradeStatus()).isEqualTo(WAIT.toString());
    }

    @DisplayName("거래 수락")
    @WithMockCustomUser
    @Test
    void acceptTrade() throws Exception {

        // given
        User seller = ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 판매자로 게시물 등록했으니 구매자로 변경
        SecurityContext buyerSecurityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        changeSecurityContext(buyerSecurityContext);
        User buyer = ((AccountContext) buyerSecurityContext.getAuthentication().getPrincipal()).getUser();
        // 구매자로 거래 신청
        TradeRegisterRespDto tradeRegisterRespDto = tradeService.registerTrade(1L);
        String tradeId = String.valueOf(tradeRegisterRespDto.getTradeId());


        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/trade/" + tradeId + "/accept")
        );


        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(result).get("data");

        TradeAcceptRespDto tradeAcceptRespDto = objectMapper.treeToValue(jsonNode, TradeAcceptRespDto.class);
        assertThat(tradeAcceptRespDto.getSellerId()).isEqualTo(seller.getId());
        assertThat(tradeAcceptRespDto.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(tradeAcceptRespDto.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(tradeAcceptRespDto.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(tradeAcceptRespDto.getTradeStatus()).isEqualTo(DEALING.toString());
    }

    @DisplayName("거래 거절")
    @WithMockCustomUser
    @Test
    void denyTrade() throws Exception {

        // given
        User seller = ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 판매자로 게시물 등록했으니 구매자로 변경
        SecurityContext buyerSecurityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        changeSecurityContext(buyerSecurityContext);
        User buyer = ((AccountContext) buyerSecurityContext.getAuthentication().getPrincipal()).getUser();
        // 구매자로 거래 신청 완료
        TradeRegisterRespDto tradeRegisterRespDto = tradeService.registerTrade(1L);

        String tradeId = String.valueOf(tradeRegisterRespDto.getTradeId());
        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/trade/" + tradeId + "/deny")
        );


        // then
        String result = resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(result).get("data");
        TradeDenyRespDto tradeDenyRespDto = objectMapper.treeToValue(jsonNode, TradeDenyRespDto.class);

        assertThat(tradeDenyRespDto.getSellerId()).isEqualTo(seller.getId());
        assertThat(tradeDenyRespDto.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(tradeDenyRespDto.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(tradeDenyRespDto.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(tradeDenyRespDto.getTradeStatus()).isEqualTo(DENY.toString());
    }

    @DisplayName("거래 완료(둘다 완료버튼)")
    @WithMockCustomUser
    @Test
    void completeTrade() throws Exception {
        // given
        SecurityContext sellerSecurityContext = SecurityContextHolder.getContext();
        User seller = ((AccountContext) sellerSecurityContext.getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 유저 1로 게시물 등록했으니 유저 2로 변경
        SecurityContext buyerSecurityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        User buyer = ((AccountContext) buyerSecurityContext.getAuthentication().getPrincipal()).getUser();

        // 구매자로 거래 신청 완료
        changeSecurityContext(buyerSecurityContext);
        TradeRegisterRespDto tradeRegisterRespDto = tradeService.registerTrade(1L);

        // 판매자이 거래 수락 완료
        changeSecurityContext(sellerSecurityContext);
        tradeService.acceptTrade(tradeRegisterRespDto.getTradeId());


        // when
        // 판매자가 거래 완료 버튼을 누른다.
        String tradeId = String.valueOf(tradeRegisterRespDto.getTradeId());
        String sellerCompleteResult = mockMvc.perform(
                                        patch("/api/trade/" + tradeId + "/complete")
                                                .with(securityContext(sellerSecurityContext))
                                       ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                        .andReturn()
                                        .getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(sellerCompleteResult).get("data");
        TradeCompleteRespDto sellerCompleteRespDto = objectMapper.treeToValue(jsonNode, TradeCompleteRespDto.class);

        assertThat(sellerCompleteRespDto.getSellerId()).isEqualTo(seller.getId());
        assertThat(sellerCompleteRespDto.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(sellerCompleteRespDto.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(sellerCompleteRespDto.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(sellerCompleteRespDto.getSellerCompleteFlag()).isEqualTo(COMPLETE_CONFIRM.toString());
        assertThat(sellerCompleteRespDto.getBuyerCompleteFlag()).isEqualTo(COMPLETE_ABSTAIN.toString());
        assertThat(sellerCompleteRespDto.getTradeStatus()).isEqualTo(DEALING.toString());


        // 구매자가 거래 완료 후 완료 버튼을 누른다.
        String buyerCompleteResult = mockMvc.perform(
                                    patch("/api/trade/" + tradeId + "/complete")
                                    .with(securityContext(buyerSecurityContext))
                                    ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                    .andReturn().getResponse().getContentAsString();

        // then
        JsonNode jsonNode2 = objectMapper.readTree(buyerCompleteResult).get("data");
        TradeCompleteRespDto buyerCompleteRespDto = objectMapper.treeToValue(jsonNode2, TradeCompleteRespDto.class);

        assertThat(buyerCompleteRespDto.getSellerId()).isEqualTo(seller.getId());
        assertThat(buyerCompleteRespDto.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(buyerCompleteRespDto.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(buyerCompleteRespDto.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(buyerCompleteRespDto.getSellerCompleteFlag()).isEqualTo(COMPLETE_CONFIRM.toString());
        assertThat(buyerCompleteRespDto.getBuyerCompleteFlag()).isEqualTo(COMPLETE_CONFIRM.toString());
        assertThat(buyerCompleteRespDto.getTradeStatus()).isEqualTo(COMPLETE.toString());
    }

    // 둘다 취소 버튼을 누르지 않으면 조율 후 관리자가 어드민 페이지로 직접 취소
    // 만장일치 취소면 취소처리
    @WithMockCustomUser
    @DisplayName("구매 취소 (판매자가 거래 완료를 눌렀으나 구매자는 거래 취소 버튼을 누름)")
    @Test
    void cancelTrade() throws Exception {

        // given
        SecurityContext sellerSecurityContext = SecurityContextHolder.getContext();
        User seller = ((AccountContext) sellerSecurityContext.getAuthentication().getPrincipal()).getUser();
        // BeforeEach 에서 판매자로 게시물 등록했으니 구매자로 변경
        SecurityContext buyerSecurityContext = securityContextFactory.createSecurityContext("kmini2", "1111", "kmini2@gmail.com");
        User buyer = ((AccountContext) buyerSecurityContext.getAuthentication().getPrincipal()).getUser();

        // 구매자 거래 신청 완료
        changeSecurityContext(buyerSecurityContext);
        TradeRegisterRespDto tradeRegisterRespDto = tradeService.registerTrade(1L);

        // 판매자 거래 수락 완료
        changeSecurityContext(sellerSecurityContext);
        tradeService.acceptTrade(tradeRegisterRespDto.getTradeId());

        // when
        // 판매자가 거래 완료 버튼을 누른다.
        String tradeId = String.valueOf(tradeRegisterRespDto.getTradeId());
        String completeResult = mockMvc.perform(
                        patch("/api/trade/" + tradeId + "/complete")
                                .with(securityContext(sellerSecurityContext))
                ).andReturn()
                .getResponse().getContentAsString();

        // 구매자가 변심하여 취소 버튼을 누른다.
        String cancelResult = mockMvc.perform(
                        patch("/api/trade/" + tradeId + "/cancel")
                                .with(securityContext(buyerSecurityContext))
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(cancelResult).get("data");
        TradeCancelRespDto buyerCancelResult = objectMapper.treeToValue(jsonNode, TradeCancelRespDto.class);
        System.out.println("buyerCancelResult = " + buyerCancelResult);

        assertThat(buyerCancelResult.getSellerId()).isEqualTo(seller.getId());
        assertThat(buyerCancelResult.getSellerName()).isEqualTo(seller.getUsername());
        assertThat(buyerCancelResult.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(buyerCancelResult.getBuyerName()).isEqualTo(buyer.getUsername());
        assertThat(buyerCancelResult.getTradeStatus()).isEqualTo(DEALING.toString());
    }


    private void changeSecurityContext(SecurityContext user2SecurityContext) {
        SecurityContextHolder.setContext(user2SecurityContext);
    }
}