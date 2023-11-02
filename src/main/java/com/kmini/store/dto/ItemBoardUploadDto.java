package com.kmini.store.dto;

import com.kmini.store.domain.ItemBoard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *  거래 게시판 업로드
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBoardUploadDto {

    private Long categoryId;
    private String content;
    private MultipartFile file;
    private String itemName;

    public ItemBoard toEntity(String imageUrl) {
        return ItemBoard.builder()
                .content(content)
                .thumbnail(imageUrl)
                .itemName(itemName)
                .build();
    }

}

