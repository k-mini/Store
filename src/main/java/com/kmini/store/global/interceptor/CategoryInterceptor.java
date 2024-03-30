package com.kmini.store.global.interceptor;

import com.kmini.store.domain.board.category.dto.CategoryResponseDto;
import com.kmini.store.domain.board.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {

    private List<CategoryResponseDto> categories;
    private final CategoryService categoryService;

    // 원래 빈(bean) 초기화 시점에 하려고 했으나
    // h2 데이터 베이스에서 applicationRunner 시점에 데이터가 들어가기 때문에
    // 빈 초기화 시점에는 데이터가 없다.
    @PostConstruct
    public void init() {
       this.categories = categoryService.selectCategories();
    }

    public void reload() {
        this.categories = categoryService.selectCategories();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) {
            return ;
        }

        ModelMap modelMap = modelAndView.getModelMap();
        String viewName = modelAndView.getViewName();
        log.trace("modelMap = {} ", modelMap);

        modelMap.addAttribute("categories", categories);
        Object subCategory = modelMap.getAttribute("subCategory");

        if (subCategory != null && viewName != null && !viewName.startsWith("redirect:")) {
            String keyword = (String) subCategory;
            if (keyword.equals("all")) {
                return ;
            }
            CategoryResponseDto category = findCategory(keyword);
            modelMap.addAttribute("subCategoryKoName",category.getCategoryKoName());
        }
    }

    private CategoryResponseDto findCategory(String keyword) {

        for (CategoryResponseDto categoryResponseDto : categories) {
            if (categoryResponseDto.getCategoryName().equals(keyword.toUpperCase())) {
                return categoryResponseDto;
            }
            for (CategoryResponseDto childCategory : categoryResponseDto.getSubCategories()) {
                if (childCategory.getCategoryName().equals(keyword.toUpperCase())) {
                    return childCategory;
                }
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리 정보입니다.");
    }
}
