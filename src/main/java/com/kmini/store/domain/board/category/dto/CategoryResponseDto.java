package com.kmini.store.domain.board.category.dto;

import com.kmini.store.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
public class CategoryResponseDto {

    private String categoryName;
    private String categoryKoName;
    private List<CategoryResponseDto> subCategories;

    public static List<CategoryResponseDto> toDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public static CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .categoryKoName(category.getCategoryKoName())
                .categoryName(category.getCategoryName())
                .subCategories(
                        category.getChildCategories().size() > 0 ? CategoryResponseDto.toDtos(category.getChildCategories()) : new ArrayList<>()
                )
                .build();
    }
}
