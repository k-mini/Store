package com.kmini.store.dto;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.CommentDto.ItemBoardCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ItemBoardRespDto {


    // 거래 게시글 목록 Dto
    @Data
    @AllArgsConstructor @Builder
    public static class ItemBoardRespAllDto {
        // 게시물 ID
        private Long id;
        // 작성자 이름
        private String username;
        // 제목
        private String title;
        // 제목아래 내용 살짝..
        private String content;
        // 게시물 썸네일
        private String thumbnail;
        // 작성 시간
        private String createdDate;

        public static ItemBoardRespAllDto toDto(ItemBoard itemBoard) {
            String createdDate = CustomTimeUtils.convertTime(itemBoard.getCreatedDate());
            return ItemBoardRespAllDto.builder()
                    .id(itemBoard.getId())
                    .username(itemBoard.getUser().getUsername())
                    .title(itemBoard.getTitle())
                    .content(itemBoard.getContent())
                    .thumbnail(itemBoard.getThumbnail())
                    .createdDate(createdDate)
                    .build();
        }
    }

    // 게시글 자세히 보기 Dto
    @Data
    @AllArgsConstructor @Builder
    public static class ItemBoardRespDetailDto {
        // 게시물 ID
        private Long id;
        // 작성자 이메일
        private String email;
        // 작성자 이름
        private String username;
        // 유저 썸네일
        private String userThumbnail;
        // 제목
        private String title;
        // 게시물 썸네일
        private String boardThumbnail;
        // 내용
        private String content;
        // 작성 시간
        private String createdDate;
        // 조회 수
        private int views;
        // 댓글 수
        private int commentNumber;
        // 댓글
        private List<ItemBoardCommentDto> commentDtos = new ArrayList<>();

        public static ItemBoardRespDetailDto toDto(ItemBoard itemBoard, List<Comment> comment) {
            String createdDate = CustomTimeUtils.convertTime(itemBoard.getCreatedDate());

            return ItemBoardRespDetailDto.builder()
                    .id(itemBoard.getId())
                    .email(itemBoard.getUser().getEmail())
                    .username(itemBoard.getUser().getUsername())
                    .userThumbnail(itemBoard.getUser().getThumbnail())
                    .title(itemBoard.getTitle())
                    .boardThumbnail(itemBoard.getThumbnail())
                    .content(itemBoard.getContent())
                    .createdDate(createdDate)
                    .views(itemBoard.getViews())
                    .commentNumber(comment.size())
                    .commentDtos(ItemBoardCommentDto.toDtos(comment))
                    .build();
        }
    }
}
