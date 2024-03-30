package com.kmini.store.domain.admin.comment.dto;

import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminCommentResponseDto<T> {

    int draw;
    Page<T> page;

    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class AdminCommentDto {

        private Long commentId;
        private String content;
        private Long boardId;
        private Long topCommentId;
        private Long userId;
        private String username;
        private String createdDate;
        private String lastModifiedDate;

        public static AdminCommentDto toDto(Comment comment) {
            return AdminCommentDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .boardId(comment.getBoard().getId())
                    .topCommentId(comment.getTopComment() != null ? comment.getTopComment().getId() : null)
                    .userId(comment.getUser().getId())
                    .username(comment.getUser().getUsername())
                    .createdDate(CustomTimeUtils.convertTime(comment.getCreatedDate()))
                    .lastModifiedDate(CustomTimeUtils.convertTime(comment.getLastModifiedDate()))
                    .build();
        }
    }
}
