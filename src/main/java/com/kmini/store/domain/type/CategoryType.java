package com.kmini.store.domain.type;

import com.kmini.store.ex.CustomCategoryNotFoundDtypeException;
import lombok.Getter;

@Getter
public enum CategoryType {
    TRADE("거래"),
    COMMUNITY("커뮤니티"),
    ELECTRONICS("전자기기"),
    FOODS("음식"),
    FREE("자유"),
    QNA("질문");

    private final String koName;

    CategoryType(String koName) {
        this.koName = koName;
    }


    public String getNameWithLowerCase() {
        return this.name().toLowerCase();
    }



}
