package com.kmini.store.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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
