package com.kmini.store.controller.api;

import com.kmini.store.dto.response.CategoryRespDto;
import com.kmini.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
        List<CategoryRespDto> result = categoryService.selectCategories();

        return ResponseEntity
                .ok(result);
    }
}
