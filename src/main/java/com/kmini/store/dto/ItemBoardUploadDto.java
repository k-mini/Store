package com.kmini.store.dto;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.ItemBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBoardUploadDto {

    private Long categoryId;
    private String content;
    private String thumbnail;
    private String itemName;

    public ItemBoard toEntity() {
        return ItemBoard.builder()
                .category(new BoardCategory(categoryId))
                .content(content)
                .thumbnail(thumbnail)
                .itemName(itemName)
                .build();

    }
}
