package com.kmini.store.dto.request;

import com.kmini.store.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @AllArgsConstructor @Builder
    @NoArgsConstructor
    public static class BoardCommentReqDto {
        // 게시물 id
        private Long boardId;
        // 내용
        @NotEmpty(message = "댓글을 입력해 주세요.")
        private String content;
    }

    @Data
    @AllArgsConstructor @Builder
    @NoArgsConstructor
    public static class BoardReplyReqDto {
        // 게시물 id
        private Long boardId;
        // 상위 댓글 id
        private Long topCommentId;
        // 답글 내용
        private String content;
    }
}
