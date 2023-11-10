package com.kmini.store.dto.response;

import com.kmini.store.config.util.CustomTimeUtils;
import com.kmini.store.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BoardDto {
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
    // 작성 시간
    private String createdDate;

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .username(board.getUser().getUsername())
                .title(board.getTitle())
                .content(getMinContent(10, board))
                .thumbnail(board.getThumbnail())
                .createdDate(CustomTimeUtils.convertTime(board.getCreatedDate()))
                .build();
    }

    private static String getMinContent(int cnt, Board board) {
        int lastIndex = Math.min(cnt, board.getContent().length());
        return board.getContent().substring(0, lastIndex) + "...";
    }
}
