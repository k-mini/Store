package com.kmini.store.domain.type;

public enum TradeStatus {
    WAIT("수락 대기중"),
    DEALING("거래 진행중"),
    COMPLETE("거래 완료"),
    CANCEL("거래 취소");

    private String message;

    TradeStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
