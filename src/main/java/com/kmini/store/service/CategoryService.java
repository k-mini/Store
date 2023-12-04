package com.kmini.store.service;


import com.kmini.store.domain.Category;
import com.kmini.store.dto.response.CategoryRespDto;
import com.kmini.store.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public MultiValueMap<CategoryRespDto, CategoryRespDto> retrieveCategories() {

        // 방법 1
//        Map<String, List<String>> result = categoryRepository.findAll()
//                .stream()
//                .filter(category -> category.getSuperCategory() == null)
//                .collect(Collectors.toMap(Category::getCategoryName, category->category.getChildCategories().stream().map(Category::getCategoryName).collect(Collectors.toList())));

        // 방법 2
        List<Category> superCategories = categoryRepository.findAll()
                                        .stream()
                                        .filter(category -> category.getSuperCategory() == null).collect(Collectors.toList());
        MultiValueMap<CategoryRespDto, CategoryRespDto> map = new LinkedMultiValueMap<>();

        for (Category category : superCategories) {
            CategoryRespDto categoryRespDto = CategoryRespDto.toDto(category);
            map.put(categoryRespDto, categoryRespDto.getSubCategories());
        }
        return map;
    }
}
