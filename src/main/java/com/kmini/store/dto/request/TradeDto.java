package com.kmini.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TradeDto {

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeReqDto {
        // 신청 게시물 번호
        private Long boardId;
        // 신청 유저 id
        private Long buyerId;
    }
}
