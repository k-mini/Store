package com.kmini.store.dto.response;

import com.kmini.store.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TradeDto {

    @Data
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class TradeRespDto {
        // 생성 거래 번호
        private Long tradeId;

        public static TradeRespDto toDto(Trade savedTrade) {
            return TradeRespDto.builder()
                    .tradeId(savedTrade.getId())
                    .build();
        }
    }

}
