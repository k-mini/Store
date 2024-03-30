package com.kmini.store.domain.board.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TradeRequestDto {

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeReqDto {
        // 신청 게시물 번호
        private Long boardId;
        // 신청 유저 id
        private Long buyerId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectUserTradeHistoryReqDto {
        // 유저 번호
        private Long userId;
        // 검색 조건
        private String sType;
        // 검색어
        private String s;
    }
}
