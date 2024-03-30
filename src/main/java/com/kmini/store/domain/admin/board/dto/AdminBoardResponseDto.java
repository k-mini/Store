package com.kmini.store.domain.admin.board.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.entity.Category;
import lombok.*;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminBoardResponseDto<T> {

    int draw;
    @JsonIgnoreProperties("content.user")
    Page<T> page;

    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class AdminBoardDto {
        private Long boardId;
        private Long userId;
        private String username;
        private String title;
        private String content;
        private AdminCategoryResponseDto category;
        private AdminCategoryResponseDto subCategory;
        private long views;
        private String createdDate;
        private String lastModifiedDate;

        public static AdminBoardDto toDto(Board board) {
            return AdminBoardDto.builder()
                    .boardId(board.getId())
                    .userId(board.getUser().getId())
                    .username(board.getUser().getUsername())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .category(AdminCategoryResponseDto.toDto(board.getCategory()))
                    .subCategory(AdminCategoryResponseDto.toDto(board.getSubCategory()))
                    .views(board.getViews())
                    .createdDate(CustomTimeUtils.convertTime(board.getCreatedDate()))
                    .lastModifiedDate(CustomTimeUtils.convertTime(board.getLastModifiedDate()))
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class AdminCategoryResponseDto {
        private String categoryName;
        private String categoryKoName;

        public static AdminCategoryResponseDto toDto(Category category) {
            return AdminCategoryResponseDto.builder()
                    .categoryName(category.getCategoryName())
                    .categoryKoName(category.getCategoryKoName())
                    .build();
        }
    }
}
