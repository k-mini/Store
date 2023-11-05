package com.kmini.store.dto.search;

import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.SearchDto.SearchBoardDto;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class BoardSearchCond {

    private CategoryType categoryName;

    private CategoryType subCategoryName;
    
    // 제목
    private String title;
    // 내용
    private String content;

    public BoardSearchCond(CategoryType categoryName, CategoryType subCategoryName, SearchBoardDto searchBoardDto) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.title = "title".equals(searchBoardDto.getSType()) ? searchBoardDto.getS() : null ;
        this.content = "content".equals(searchBoardDto.getSType()) ? searchBoardDto.getS() : null ;
    }

}
