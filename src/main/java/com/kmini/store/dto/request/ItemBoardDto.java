package com.kmini.store.dto.request;

import com.kmini.store.domain.ItemBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class ItemBoardDto {

    @Data
    @AllArgsConstructor
    @Builder
    public static class ItemBoardUpdateFormDto {

        // 게시물 ID
        private Long boardId;
        // 제목
        @NotBlank
        private String title;
        // 내용
        @NotBlank
        private String content;
        // 아이템 이름
        private String itemName;
        // 게시물 썸네일
        private MultipartFile file;
        // 서브 카테고리
        private String subCategory;

        public static ItemBoardUpdateFormDto toDto(ItemBoard board) {
            return ItemBoardUpdateFormDto.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
//                    .file(board.getThumbnail())
                    .build();
        }
    }
}
