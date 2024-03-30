package com.kmini.store.domain.board.common.dto;

import com.kmini.store.global.util.PageAttr;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
public class BoardListApiResponseDto {
    private int draw;
    private Page<BoardResponseDto> page;
    private PageAttr pageAttr;
    private String searchType;
    private String searchKeyword;
}
