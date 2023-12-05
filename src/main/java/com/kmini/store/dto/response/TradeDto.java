package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Category;
import com.kmini.store.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

public class TradeDto {

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeRegisterRespDto {

        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;
        private String tradeStatus;

        public static TradeRegisterRespDto toDto(Trade savedTrade) {
            return TradeRegisterRespDto.builder()
                    .tradeId(savedTrade.getId())
                    .sellerId(savedTrade.getSeller().getId())
                    .sellerName(savedTrade.getSeller().getUsername())
                    .buyerId(savedTrade.getBuyer().getId())
                    .buyerName(savedTrade.getBuyer().getUsername())
                    .tradeStatus(savedTrade.getTradeStatus().name())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeAcceptRespDto {

        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;
        private String tradeStatus;

        public static TradeAcceptRespDto toDto(Trade acceptedTrade) {
            return TradeAcceptRespDto.builder()
                    .tradeId(acceptedTrade.getId())
                    .sellerId(acceptedTrade.getSeller().getId())
                    .sellerName(acceptedTrade.getSeller().getUsername())
                    .buyerId(acceptedTrade.getBuyer().getId())
                    .buyerName(acceptedTrade.getBuyer().getUsername())
                    .tradeStatus(acceptedTrade.getTradeStatus().name())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeDenyRespDto {

        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;
        private String tradeStatus;

        public static TradeDenyRespDto toDto(Trade deniedTrade) {
            return TradeDenyRespDto.builder()
                    .tradeId(deniedTrade.getId())
                    .sellerId(deniedTrade.getSeller().getId())
                    .sellerName(deniedTrade.getSeller().getUsername())
                    .buyerId(deniedTrade.getBuyer().getId())
                    .buyerName(deniedTrade.getBuyer().getUsername())
                    .tradeStatus(deniedTrade.getTradeStatus().name())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeCompleteRespDto {

        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;
        private String tradeStatus;
        private String buyerCompleteFlag;
        private String sellerCompleteFlag;

        public static TradeCompleteRespDto toDto(Trade completedTrade) {
            return TradeCompleteRespDto.builder()
                    .tradeId(completedTrade.getId())
                    .sellerId(completedTrade.getSeller().getId())
                    .sellerName(completedTrade.getSeller().getUsername())
                    .buyerId(completedTrade.getBuyer().getId())
                    .buyerName(completedTrade.getBuyer().getUsername())
                    .tradeStatus(completedTrade.getTradeStatus().name())
                    .buyerCompleteFlag(completedTrade.getBuyerCompleteFlag().toString())
                    .sellerCompleteFlag(completedTrade.getSellerCompleteFlag().toString())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeCancelRespDto {

        private Long tradeId;
        private Long sellerId;
        private String sellerName;
        private Long buyerId;
        private String buyerName;
        private String tradeStatus;
        private String buyerCompleteFlag;
        private String sellerCompleteFlag;

        public static TradeCancelRespDto toDto(Trade canceledTrade) {
            return TradeCancelRespDto.builder()
                    .tradeId(canceledTrade.getId())
                    .sellerId(canceledTrade.getSeller().getId())
                    .sellerName(canceledTrade.getSeller().getUsername())
                    .buyerId(canceledTrade.getBuyer().getId())
                    .buyerName(canceledTrade.getBuyer().getUsername())
                    .tradeStatus(canceledTrade.getTradeStatus().name())
                    .buyerCompleteFlag(canceledTrade.getBuyerCompleteFlag().toString())
                    .sellerCompleteFlag(canceledTrade.getSellerCompleteFlag().toString())
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
        // 대 카테고리
        private String superCategoryName;
        // 대 카테고리 한국어
        private String superCategoryKoName;
        // 소 카테고리
        private String subCategoryName;
        // 소 카테고리 한국어
        private String subCategoryKoName;
        // 거래 시작 날짜
        private String createdDate;

        public static TradeHistoryRespDto toDto(Trade trade, Map<Long, Category> superCategoryMap, Map<Long,Category> subCategoryMap) {
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
                    .superCategoryName(superCategoryMap.get(trade.getBoard().getId()).getCategoryName().toLowerCase())
                    .superCategoryKoName(superCategoryMap.get(trade.getBoard().getId()).getCategoryKoName())
                    .subCategoryName(subCategoryMap.get(trade.getBoard().getId()).getCategoryName().toLowerCase())
                    .subCategoryKoName(subCategoryMap.get(trade.getBoard().getId()).getCategoryKoName())
                    .createdDate(CustomTimeUtils.convertTime(trade.getCreatedDate()))
                    .build();
        }
    }

}
