package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ItemBoardDto {


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
        // 내용
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
                    .content(getMinContent(10, itemBoard))
                    .thumbnail(itemBoard.getThumbnail())
                    .createdDate(createdDate)
                    .build();
        }

        private static String getMinContent(int cnt, ItemBoard itemBoard) {
            int lastIndex = Math.min(cnt, itemBoard.getContent().length());
            return itemBoard.getContent().substring(0, lastIndex) + "...";
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
        // 작성자 id
        private Long writerId;
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
        private int commentTotalCount;
        // 댓글
        private List<BoardCommentRespDto> comments = new ArrayList<>();

        public static ItemBoardRespDetailDto toDto(ItemBoard itemBoard, List<Comment> comments) {
            return ItemBoardRespDetailDto.builder()
                    .id(itemBoard.getId())
                    .email(itemBoard.getUser().getEmail())
                    .writerId(itemBoard.getUser().getId())
                    .username(itemBoard.getUser().getUsername())
                    .userThumbnail(itemBoard.getUser().getThumbnail())
                    .title(itemBoard.getTitle())
                    .boardThumbnail(itemBoard.getThumbnail())
                    .content(itemBoard.getContent())
                    .createdDate(CustomTimeUtils.convertTime(itemBoard.getCreatedDate()))
                    .views(itemBoard.getViews())
                    .commentTotalCount(itemBoard.getComments().size())
                    .comments(BoardCommentRespDto.toDtos(comments))
                    .build();
        }
    }
}
