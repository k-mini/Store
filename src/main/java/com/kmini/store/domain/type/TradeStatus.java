package com.kmini.store.domain.type;

public enum TradeStatus {
    WAIT("대기"),
    DEALING("거래중"),
    COMPLETE("완료"),
    CANCEL("취소");

    private String message;

    TradeStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
