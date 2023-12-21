package com.kmini.store.dto.response;

import com.kmini.store.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class CategoryRespDto {

    private String categoryName;
    private String categoryKoName;
    private List<CategoryRespDto> subCategories;

    public static CategoryRespDto toDto(Category category) {
        return CategoryRespDto.builder()
                .categoryKoName(category.getCategoryKoName())
                .categoryName(category.getCategoryName())
                .subCategories(category.getChildCategories().size() > 0 ?
                        category.getChildCategories().stream().map(CategoryRespDto::toDto).collect(Collectors.toList()) : null)
                .build();
    }
}
