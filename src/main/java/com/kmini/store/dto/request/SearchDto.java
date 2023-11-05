package com.kmini.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SearchDto {

    @Data
    @AllArgsConstructor
    public static class SearchBoardListDto {
        private String searchType;
        private String searchWord;
    }

}
