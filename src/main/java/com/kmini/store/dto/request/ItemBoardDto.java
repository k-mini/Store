package com.kmini.store.dto.request;

import com.kmini.store.domain.ItemBoard;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ItemBoardDto {

    /**
     *  거래 게시판 업로드
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemBoardSaveReqDto {

        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        private MultipartFile file;
        private String itemName;
        private Double latitude;
        private Double longitude;
    }

    /**
     *  거래 게시판 업로드
     */
    @Getter
    @AllArgsConstructor @NoArgsConstructor
    public static class ItemBoardSaveReqApiDto {

        @NotEmpty
        private String title;
        @NotEmpty
        private String content;
        @NotEmpty
        private String itemName;
        private Double latitude;
        private Double longitude;
    }

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
        private Double latitude;
        private Double longitude;


        public static ItemBoardUpdateReqDto toDto(ItemBoard itemBoard) {
            return ItemBoardUpdateReqDto.builder()
//                    .boardId(board.getId())
                    .title(itemBoard.getTitle())
                    .content(itemBoard.getContent())
                    .file(itemBoard.getFile())
                    .latitude(itemBoard.getLatitude())
                    .longitude(itemBoard.getLongitude())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class ItemBoardUpdateReqApiDto {

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
        private Double latitude;
        private Double longitude;

        public static ItemBoardUpdateReqApiDto toDto(ItemBoard itemBoard) {
            return ItemBoardUpdateReqApiDto.builder()
                    .itemName(itemBoard.getItemName())
                    .title(itemBoard.getTitle())
                    .content(itemBoard.getContent())
                    .latitude(itemBoard.getLatitude())
                    .longitude(itemBoard.getLongitude())
                    .build();
        }
    }
}
