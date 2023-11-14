package com.kmini.store.dto.request;

import com.kmini.store.domain.CommunityBoard;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.type.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

public class BoardDto {

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
    public static class ItemBoardFormSaveDto {

        private String subCategory;
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        private MultipartFile file;
        private String itemName;

        public ItemBoard toEntity() {
            return ItemBoard.builder()
                    .title(title)
                    .content(content)
                    .itemName(itemName)
                    .build();
        }

        public CategoryType getSubCategoryType() {
            return CategoryType.valueOf(this.getSubCategory().toUpperCase());
        }
    }

    /**
     *  커뮤니티 게시판 업로드
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommunityBoardFormSaveDto {

        private String subCategory;
        private String title;
        private String content;
        private MultipartFile file;

        public CommunityBoard toEntity() {
            return CommunityBoard.builder()
                    .title(title)
                    .content(content)
                    .build();
        }

        public CategoryType getSubCategoryType() {
            return CategoryType.valueOf(subCategory.toUpperCase());
        }
    }

}
