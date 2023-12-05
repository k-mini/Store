package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class BoardCommentRespDto {
        // comment id
        private Long id;
        // 작성자 id
        private Long commentUserId;
        // 작성자 이름
        private String commentUserName;
        // 내용
        private String content;
        // 답글
        private List<BoardReplyRespDto> replies;
        // 생성 시간
        private String createdDate;

        public static List<BoardCommentRespDto> toDtos(List<Comment> comments) {
            return comments.stream()
                    .map(BoardCommentRespDto::toDto)
                    .collect(Collectors.toList());
        }
        public static BoardCommentRespDto toDto(Comment comment) {
            return BoardCommentRespDto.builder()
                    .id(comment.getId())
                    .commentUserId(comment.getUser().getId())
                    .commentUserName(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .replies(BoardReplyRespDto.toDtos(comment.getSubComments()))
                    .createdDate(CustomTimeUtils.convertTime(comment.getCreatedDate()))
                    .build();
        }
    }

    @Data
    @AllArgsConstructor @Builder
    @NoArgsConstructor
    public static class BoardReplyRespDto {
        // 상위 댓글 id
        private Long topCommentId;
        // 답글 id
        private Long replyId;
        // 작성자 id
        private Long replyUserId;
        // 작성자 이름
        private String replyUserName;
        // 답글 내용
        private String content;
        // 생성 시간
        private String createdDate;

        public static List<BoardReplyRespDto> toDtos(List<Comment> comment) {
            return comment.stream()
                    .map(BoardReplyRespDto::toDto)
                    .collect(Collectors.toList());
        }

        public static BoardReplyRespDto toDto(Comment reply) {
            return BoardReplyRespDto.builder()
                    .topCommentId(reply.getTopComment().getId())
                    .replyId(reply.getId())
                    .replyUserId(reply.getUser().getId())
                    .replyUserName(reply.getUser().getUsername())
                    .content(reply.getContent())
                    .createdDate(CustomTimeUtils.convertTime(reply.getCreatedDate()))
                    .build();
        }
    }
}
