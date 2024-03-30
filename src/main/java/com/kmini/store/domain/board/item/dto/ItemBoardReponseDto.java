package com.kmini.store.domain.board.item.dto;

import com.kmini.store.domain.comment.dto.CommentResponseDto;
import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.entity.Comment;
import com.kmini.store.domain.entity.ItemBoard;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ItemBoardReponseDto {

    // 거래 게시글 목록 Dto
    @Getter
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

    // 게시물 수정 반환 Dto
    @Getter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class ItemBoardUpdateRespDto {
        // 게시물 ID
        private Long id;
        // 상품 이름
        private String itemName;
        // 작성자 이메일
        private String email;
        // 작성자 id
        private Long writerId;
        // 작성자 이름
        private String username;
        // 제목
        private String title;
        // 내용
        private String content;
        // 작성 시간
        private String createdDate;

        public static ItemBoardUpdateRespDto toDto(ItemBoard itemBoard) {
            return ItemBoardUpdateRespDto.builder()
                    .id(itemBoard.getId())
                    .itemName(itemBoard.getItemName())
                    .email(itemBoard.getUser().getEmail())
                    .writerId(itemBoard.getUser().getId())
                    .username(itemBoard.getUser().getUsername())
                    .title(itemBoard.getTitle())
                    .content(itemBoard.getContent())
                    .createdDate(CustomTimeUtils.convertTime(itemBoard.getCreatedDate()))
                    .build();
        }
    }


    // 게시글 자세히 보기 Dto
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class ItemBoardViewRespDto {
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
        // 게시물 이미지
        private List<String> itemImageURLs;
        // 내용
        private String content;
        // 작성 시간
        private String createdDate;
        // 조회 수
        private long views;
        // 댓글 수
        private int commentTotalCount;
        // 댓글
        private List<CommentResponseDto.BoardCommentUpdateRespDto> comments = new ArrayList<>();
        // 거래 상태
        private boolean tradePossible;
        // 위도
        private Double latitude;
        // 경도
        private Double longitude;

        public static ItemBoardViewRespDto toDto(ItemBoard itemBoard, List<Comment> comments, boolean tradePossible) {
            return ItemBoardViewRespDto.builder()
                    .id(itemBoard.getId())
                    .email(itemBoard.getUser().getEmail())
                    .writerId(itemBoard.getUser().getId())
                    .username(itemBoard.getUser().getUsername())
                    .userThumbnail(itemBoard.getUser().getThumbnail())
                    .title(itemBoard.getTitle())
                    .boardThumbnail(itemBoard.getThumbnail())
                    .itemImageURLs(ItemBoardReponseDto.convertStrURLsToListURLs(itemBoard.getItemImageURLs()))
                    .content(itemBoard.getContent())
                    .createdDate(CustomTimeUtils.convertTime(itemBoard.getCreatedDate()))
                    .views(itemBoard.getViews())
                    .commentTotalCount(itemBoard.getComments().size())
                    .comments(CommentResponseDto.BoardCommentUpdateRespDto.toDtos(comments))
                    .tradePossible(tradePossible)
                    .latitude(itemBoard.getLatitude())
                    .longitude(itemBoard.getLongitude())
                    .build();
        }
    }


    // 게시물 저장
    @Getter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class ItemBoardSaveRespDto {
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

        public static ItemBoardSaveRespDto toDto(ItemBoard itemBoard) {
            return ItemBoardSaveRespDto.builder()
                    .id(itemBoard.getId())
                    .email(itemBoard.getUser().getEmail())
                    .writerId(itemBoard.getUser().getId())
                    .username(itemBoard.getUser().getUsername())
                    .userThumbnail(itemBoard.getUser().getThumbnail())
                    .title(itemBoard.getTitle())
                    .boardThumbnail(itemBoard.getThumbnail())
                    .content(itemBoard.getContent())
                    .createdDate(CustomTimeUtils.convertTime(itemBoard.getCreatedDate()))
                    .build();
        }
    }

    // 삭제된 게시물
    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class ItemBoardDeleteRespDto {
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

        public static ItemBoardDeleteRespDto toDto(Board board) {
            return ItemBoardDeleteRespDto.builder()
                    .id(board.getId())
                    .email(board.getUser().getEmail())
                    .writerId(board.getUser().getId())
                    .username(board.getUser().getUsername())
                    .userThumbnail(board.getUser().getThumbnail())
                    .title(board.getTitle())
                    .boardThumbnail(board.getThumbnail())
                    .content(board.getContent())
                    .createdDate(CustomTimeUtils.convertTime(board.getCreatedDate()))
                    .build();
        }
    }

    public static List<String> convertStrURLsToListURLs(String urls) {
        if (!StringUtils.hasText(urls)) {
            return new ArrayList<>();
        }

        StringTokenizer st = new StringTokenizer(urls, ",");
        List<String> itemImageURLs = new ArrayList<>();
        while (st.hasMoreTokens()) {
            itemImageURLs.add(st.nextToken());
        }
        return itemImageURLs;
    }
}
