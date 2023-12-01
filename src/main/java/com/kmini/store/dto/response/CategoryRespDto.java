package com.kmini.store.dto.response;

import com.kmini.store.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
public class CategoryRespDto {

    private String categoryName;
    private String categoryKoName;
    private List<CategoryRespDto> childCategories;

    public static CategoryRespDto toDto(Category category) {
        return CategoryRespDto.builder()
                .categoryKoName(category.getCategoryKoName())
                .categoryName(category.getCategoryName())
                .childCategories(category.getChildCategories().size() > 0 ?
                        category.getChildCategories().stream().map(CategoryRespDto::toDto).collect(Collectors.toList()) : null)
                .build();
    }
}
