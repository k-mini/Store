package com.kmini.store.dto.search;

import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.SearchDto;
import com.kmini.store.dto.request.SearchDto.SearchBoardListDto;
import lombok.Data;

@Data
public class BoardSearchCond {

    private CategoryType categoryName;

    private CategoryType subCategoryName;

    public BoardSearchCond(CategoryType categoryName, CategoryType subCategoryName, SearchBoardListDto boardListDto) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }
}
