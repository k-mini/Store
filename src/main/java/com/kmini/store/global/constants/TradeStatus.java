package com.kmini.store.global.constants;

public enum TradeStatus {
    WAIT("수락 대기중"),
    DEALING("거래 진행중"),
    COMPLETE("거래 완료"),
    CANCEL("거래 취소"),
    DENY("거래 거절");

    private String message;

    TradeStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
