package com.kmini.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemBoardUpdateDto {

    private Long boardId;
    private Long categoryId;
    private String thumbnail;
    private String content;
    private String itemName;
}
