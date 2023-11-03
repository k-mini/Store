package com.kmini.store.aop;

import com.kmini.store.config.init.CategoryHolder;
import com.kmini.store.domain.type.BoardType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CategoryAdvice {

    private final CategoryHolder categoryHolder;

    @ModelAttribute
    public void putCategories(Model model) throws Throwable {
        model.addAttribute("categories", categoryHolder.getMap());
    }
}
