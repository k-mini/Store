package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TradeDto {

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeRegisterRespDto {
        // 생성 거래 번호
        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;

        public static TradeRegisterRespDto toDto(Trade savedTrade) {
            return TradeRegisterRespDto.builder()
                    .tradeId(savedTrade.getId())
                    .sellerId(savedTrade.getSeller().getId())
                    .sellerName(savedTrade.getSeller().getUsername())
                    .buyerId(savedTrade.getBuyer().getId())
                    .buyerName(savedTrade.getBuyer().getUsername())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeHistoryRespDto {
        // 거래 번호
        private Long tradeId;
        // 게시물 번호
        private Long boardId;
        // 게시물 제목
        private String title;
        // 판매자 id
        private Long sellerId;
        // 판매자 이름
        private String sellerName;
        // 구매자 id
        private Long buyerId;
        // 구매자 이름
        private String buyerName;
        // 거래 상태
        private String tradeStatus;
        // 거래 상태 한국어
        private String tradeStatusKoName;
        // 거래 시작 날짜
        private String createdDate;

        public static TradeHistoryRespDto toDto(Trade trade) {
            return TradeHistoryRespDto.builder()
                    .tradeId(trade.getId())
                    .boardId(trade.getBoard().getId())
                    .title(trade.getBoard().getTitle())
                    .sellerId(trade.getBoard().getUser().getId())
                    .sellerName(trade.getBoard().getUser().getUsername())
                    .buyerId(trade.getBuyer().getId())
                    .buyerName(trade.getBuyer().getUsername())
                    .tradeStatus(trade.getTradeStatus().name())
                    .tradeStatusKoName(trade.getTradeStatus().getMessage())
                    .createdDate(CustomTimeUtils.convertTime(trade.getCreatedDate()))
                    .build();
        }
    }

}
