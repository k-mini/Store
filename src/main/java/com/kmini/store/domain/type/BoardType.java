package com.kmini.store.domain.type;

import lombok.Getter;

public enum BoardType {
    TRADE("거래"), COMMUNITY("커뮤니티"), ELECTRONICS("전자기기"), FOODS("음식"), FREE("자유"), QNA("질문");

    @Getter
    private final String name;

    BoardType(String name) {
        this.name = name;
    }
}
