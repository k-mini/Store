package com.kmini.store.dto.request;

import com.kmini.store.domain.ItemBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class ItemBoardDto {

    /**
     *  거래 게시판 수정
     */
    @Data
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
     *  거래 게시판 업로드
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UploadDto {

        private Long categoryId;
        private String title;
        private String content;
        private MultipartFile file;
        private String itemName;

        public ItemBoard toEntity(String imageUrl) {
            return ItemBoard.builder()
                    .title(title)
                    .content(content)
                    .thumbnail(imageUrl)
                    .itemName(itemName)
                    .build();
        }

    }
}
