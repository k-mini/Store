package com.kmini.store.domain.board.category.service;


import com.kmini.store.domain.board.category.repository.CategoryRepository;
import com.kmini.store.domain.entity.Category;
import com.kmini.store.domain.board.category.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category saveCategory(String newCategoryName, String newCategoryKoName, String superCategoryName) {

        Category superCategory = null;
        if (StringUtils.hasText(superCategoryName)) {
            superCategory = categoryRepository.findByCategoryName(superCategoryName)
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));
        }

        Category newCategory = new Category(newCategoryName, newCategoryKoName, superCategory);
        return categoryRepository.save(newCategory);
    }

    @Transactional
    public Category selectCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(()-> new IllegalArgumentException(categoryName + " 카테고리가 존재하지 않습니다."));
    }

    // 카테고리 조회
    @Transactional
    public List<CategoryResponseDto> selectCategories() {

        // 방법 1
//        Map<String, List<String>> result = categoryRepository.findAll()
//                .stream()
//                .filter(category -> category.getSuperCategory() == null)
//                .collect(Collectors.toMap(Category::getCategoryName, category->category.getChildCategories().stream().map(Category::getCategoryName).collect(Collectors.toList())));

        // 방법 2
        List<Category> superCategories = categoryRepository.findAll()
                                        .stream()
                                        .filter(category -> category.getSuperCategory() == null)
                                        .collect(Collectors.toList());

        return CategoryResponseDto.toDtos(superCategories);
    }
}