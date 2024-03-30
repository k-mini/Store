package com.kmini.store.domain.board.category;

import com.kmini.store.domain.board.category.service.CategoryService;
import com.kmini.store.domain.board.category.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> selectCategories() {
        List<CategoryResponseDto> result = categoryService.selectCategories();

        return ResponseEntity
                .ok(result);
    }
}
