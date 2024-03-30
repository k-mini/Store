package com.kmini.store.domain.board.community.dto;


import com.kmini.store.domain.comment.dto.CommentResponseDto;
import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.Comment;
import com.kmini.store.domain.entity.CommunityBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommunityBoardReponseDto {

    // 게시글 자세히 보기 Dto
    @Getter
    @AllArgsConstructor
    @Builder
    public static class CommunityBoardViewRespDto {
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
        private List<CommentResponseDto.BoardCommentUpdateRespDto> comments = new ArrayList<>();

        public static CommunityBoardViewRespDto toDto(CommunityBoard communityBoard, List<Comment> comments) {
            return CommunityBoardViewRespDto.builder()
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
                    .comments(CommentResponseDto.BoardCommentUpdateRespDto.toDtos(comments))
                    .build();
        }
    }
}
