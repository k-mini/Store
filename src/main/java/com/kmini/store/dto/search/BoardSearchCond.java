package com.kmini.store.dto.search;

import com.kmini.store.domain.type.CategoryType;
import lombok.Data;

@Data
public class BoardSearchCond {

    private CategoryType categoryName;

    private CategoryType subCategoryName;

    public BoardSearchCond(CategoryType categoryName, CategoryType subCategoryName) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }
}
