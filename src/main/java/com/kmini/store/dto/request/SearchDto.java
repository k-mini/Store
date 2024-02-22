package com.kmini.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SearchDto {

    @Data
    @AllArgsConstructor
    public static class SearchBoardDto {
        // 타입
        private String searchType;
        // 값
        private String searchKeyword;
        // 정렬
        private String order;
        // 주 카테고리
        private String category;
        // 부 카테고리
        private String subCategory;
    }

}
