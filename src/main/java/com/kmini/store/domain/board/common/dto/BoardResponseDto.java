package com.kmini.store.domain.board.common.dto;

import com.kmini.store.global.util.CustomTimeUtils;
import com.kmini.store.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardResponseDto {
    // 게시물 ID
    private Long id;
    // 작성자 이름
    private String username;
    // 제목
    private String title;
    // 제목아래 내용 살짝..
    private String content;
    // 게시물 썸네일
    private String thumbnail;
    // 조회 수
    private Long views;
    // 댓글 수
    private int commentsSize;
    // 작성 시간
    private String createdDate;

    public static BoardResponseDto toDto(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .username(board.getUser().getUsername())
                .title(board.getTitle())
                .content(getMinContent(10, board))
                .thumbnail(board.getThumbnail())
                .views(board.getViews())
                .commentsSize(board.getComments().size())
                .createdDate(CustomTimeUtils.convertTime(board.getCreatedDate()))
                .build();
    }

    private static String getMinContent(int cnt, Board board) {
        int lastIndex = Math.min(cnt, board.getContent().length());
        return board.getContent().substring(0, lastIndex) + "...";
    }
}
