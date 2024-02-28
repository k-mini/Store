package com.kmini.store.dto.response;

import com.kmini.store.config.util.PageAttr;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
public class BoardListApiResponseDto {
    private int draw;
    private Page<BoardDto> page;
    private PageAttr pageAttr;
    private String searchType;
    private String searchKeyword;
}
