package com.kmini.store.dto.request;

import com.kmini.store.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Data
    @AllArgsConstructor @Builder
    public static class ItemBoardCommentDto {
        // comment id
        private Long id;
        // 작성자 id
        private Long commentUserId;
        // 작성자 이름
        private String username;
        // 내용
        private String content;

        public static List<ItemBoardCommentDto> toDtos(List<Comment> comment) {
            return comment.stream()
                    .map(ItemBoardCommentDto::toDto)
                    .collect(Collectors.toList());
        }
        public static ItemBoardCommentDto toDto(Comment comment) {
            return ItemBoardCommentDto.builder()
                    .id(comment.getId())
                    .commentUserId(comment.getUser().getId())
                    .username(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .build();
        }
    }
}
