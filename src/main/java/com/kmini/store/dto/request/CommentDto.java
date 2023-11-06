package com.kmini.store.dto.request;

import com.kmini.store.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String content;
    }
}
