package com.kmini.store.dto.response;


import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.CommunityBoard;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class CommunityBoardDto {

    // 게시글 자세히 보기 Dto
    @Data
    @AllArgsConstructor
    @Builder
    public static class CommunityBoardRespDetailDto {
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
        private long views;
        // 댓글 수
        private int commentTotalCount;
        // 댓글
        private List<BoardCommentRespDto> comments = new ArrayList<>();

        public static CommunityBoardRespDetailDto toDto(CommunityBoard communityBoard, List<Comment> comments) {
            return CommunityBoardRespDetailDto.builder()
                    .id(communityBoard.getId())
                    .email(communityBoard.getUser().getEmail())
                    .writerId(communityBoard.getUser().getId())
                    .username(communityBoard.getUser().getUsername())
                    .userThumbnail(communityBoard.getUser().getThumbnail())
                    .title(communityBoard.getTitle())
                    .boardThumbnail(communityBoard.getThumbnail())
                    .content(communityBoard.getContent())
                    .createdDate(CustomTimeUtils.convertTime(communityBoard.getCreatedDate()))
                    .views(communityBoard.getViews())
                    .commentTotalCount(communityBoard.getComments().size())
                    .comments(BoardCommentRespDto.toDtos(comments))
                    .build();
        }
    }
}
