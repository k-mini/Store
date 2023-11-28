package com.kmini.store.domain.type;

import lombok.Getter;

@Getter
public enum CategoryType {
    ALL("전체"),
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

    public static CategoryType getType(String name) {
        return checkType(name);
    }

    public static CategoryType checkType(String name) {
        CategoryType category;
        try {
            category = CategoryType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("카테고리를 찾을 수 없습니다.",e);
        }
        return category;
    }

}
