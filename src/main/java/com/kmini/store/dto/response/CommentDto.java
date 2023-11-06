package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @AllArgsConstructor @Builder
    public static class BoardCommentResDto {
        // comment id
        private Long id;
        // 게시물 id
        private Long boardId;
        // 작성자 id
        private Long commentUserId;
        // 작성자 이름
        private String commentUserName;
        // 내용
        private String content;
        // 생성 시간
        private String createdDate;

        public static List<BoardCommentResDto> toDtos(List<Comment> comments) {
            return comments.stream()
                    .map(BoardCommentResDto::toDto)
                    .collect(Collectors.toList());
        }
        public static BoardCommentResDto toDto(Comment comment) {
            String formattedDate = CustomTimeUtils.convertTime(comment.getCreatedDate());
            return BoardCommentResDto.builder()
                    .id(comment.getId())
                    .boardId(comment.getBoard().getId())
                    .commentUserId(comment.getUser().getId())
                    .commentUserName(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .createdDate(formattedDate)
                    .build();
        }
    }
}
