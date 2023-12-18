package com.kmini.store.dto.request;

import com.kmini.store.domain.ItemBoard;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ItemBoardDto {

    @Data
    @AllArgsConstructor
    @Builder
    public static class ItemBoardUpdateReqDto {

        // 제목
        @NotBlank
        private String title;
        // 내용
        @NotBlank
        private String content;
        // 게시물 썸네일
        private MultipartFile file;
        // 서브 카테고리
        private String subCategory;


        public static ItemBoardUpdateReqDto toDto(ItemBoard board) {
            return ItemBoardUpdateReqDto.builder()
//                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .file(board.getFile())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class ItemBoardUpdateReqApiDto {

        // 게시물 ID
        @NotNull
        private Long boardId;
        // 상품명
        @NotBlank
        private String itemName;
        // 제목
        @NotBlank
        private String title;
        // 내용
        @NotBlank
        private String content;

        private String subCategory;

        public static ItemBoardUpdateReqApiDto toDto(ItemBoard board) {
            return ItemBoardUpdateReqApiDto.builder()
                    .boardId(board.getId())
                    .itemName(board.getItemName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .build();
        }
    }
}
