package com.kmini.store.domain.type;

import com.kmini.store.ex.CustomCategoryNotFoundDtypeException;
import lombok.Getter;

@Getter
public enum BoardType {
    TRADE("거래","I"),
    COMMUNITY("커뮤니티","C"),
    ELECTRONICS("전자기기",null),
    FOODS("음식",null),
    FREE("자유",null),
    QNA("질문",null);

    private final String name;
    private final String dtype;

    BoardType(String name, String dtype) {
        this.name = name;
        this.dtype = dtype;
    }

    public String getDtype() {
        if (this.dtype == null) {
            throw new CustomCategoryNotFoundDtypeException("dtype이 없습니다.");
        }
        return dtype;
    }

}
