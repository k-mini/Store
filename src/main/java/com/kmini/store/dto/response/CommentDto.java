package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class BoardCommentSaveRespDto {
        // comment id
        private Long id;
        // topComment id
        private Long topCommentId;
        // 작성자 id
        private Long commentUserId;
        // 작성자 이름
        private String commentUserName;
        // 내용
        private String content;
        // 답글
        private List<BoardCommentSaveRespDto> replies;
        // 생성 시간
        private String createdDate;

        public static List<BoardCommentSaveRespDto> toDtos(List<Comment> comments) {
            return comments.stream()
                    .map(BoardCommentSaveRespDto::toDto)
                    .collect(Collectors.toList());
        }
        public static BoardCommentSaveRespDto toDto(Comment comment) {
            return BoardCommentSaveRespDto.builder()
                    .id(comment.getId())
                    .topCommentId(comment.getTopComment() != null ? comment.getTopComment().getId() : null)
                    .commentUserId(comment.getUser().getId())
                    .commentUserName(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .replies(BoardCommentSaveRespDto.toDtos(comment.getSubComments()))
                    .createdDate(CustomTimeUtils.convertTime(comment.getCreatedDate()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor @Builder
    public static class BoardCommentUpdateRespDto {
        // comment id
        private Long id;
        // topComment id
        private Long topCommentId;
        // 작성자 id
        private Long commentUserId;
        // 작성자 이름
        private String commentUserName;
        // 내용
        private String content;
        // 답글
        private List<BoardCommentUpdateRespDto> replies;
        // 생성 시간
        private String createdDate;

        public static List<BoardCommentUpdateRespDto> toDtos(List<Comment> comments) {
            return comments.stream()
                    .map(BoardCommentUpdateRespDto::toDto)
                    .collect(Collectors.toList());
        }
        public static BoardCommentUpdateRespDto toDto(Comment comment) {
            return BoardCommentUpdateRespDto.builder()
                    .id(comment.getId())
                    .topCommentId(comment.getTopComment() != null ? comment.getTopComment().getId() : null)
                    .commentUserId(comment.getUser().getId())
                    .commentUserName(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .replies(BoardCommentUpdateRespDto.toDtos(comment.getSubComments()))
                    .createdDate(CustomTimeUtils.convertTime(comment.getCreatedDate()))
                    .build();
        }
    }
}
