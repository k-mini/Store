package com.kmini.store.domain.board.common.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class BoardRequestDto {

    /**
     *  거래 게시판 수정
     */
    @Getter
    @AllArgsConstructor
    @Builder
    public static class UpdateDto {

        private Long boardId;
        private Long categoryId;
        private String thumbnail;
        private String content;
        private String itemName;
    }

    /**
     *  커뮤니티 게시판 업로드
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommunityBoardSaveReqDto {

        private String subCategoryName;
        private String title;
        private String content;
        private MultipartFile file;
    }
}
