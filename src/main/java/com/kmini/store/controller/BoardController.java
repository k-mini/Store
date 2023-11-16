package com.kmini.store.controller;

import com.kmini.store.aop.CategoryHolder;
import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.SearchDto.SearchBoardDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/boards/{category}/{subCategory}")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 카테고리별 게시물 조회
    @GetMapping
    public String load(
            @PathVariable("category") String categoryName,
            @PathVariable("subCategory") String subCategoryName,
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @ModelAttribute SearchBoardDto searchBoardDto,
            Model model) {
        log.debug("category = {}, subCategory = {}", categoryName, subCategoryName);
        if (log.isDebugEnabled()) {
            Sort sort = pageable.getSort();
            log.debug("Sort = {}", sort);
            for (Sort.Order order : sort) {
                log.debug("Order = {}", order);
            }
        }
        log.debug("SearchBoardDto = {}", searchBoardDto);

        String order = searchBoardDto.getOrder();
        if (StringUtils.hasText(order) && CustomPageUtils.isValid(order)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, order, "createdDate"));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, "createdDate"));
        }

        Page<BoardDto> results = boardService.load(pageable, categoryName, subCategoryName, searchBoardDto);
        for (BoardDto result : results.getContent()) {
            log.debug("result = {}", result);
        }

        CustomPageUtils.configure(results, 5, model);
        model.addAttribute("sType", searchBoardDto.getSType());
        model.addAttribute("s", searchBoardDto.getS());
        model.addAttribute("results", results);
        return "board/boardList";
    }
}

