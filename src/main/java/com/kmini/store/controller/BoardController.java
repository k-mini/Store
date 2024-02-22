package com.kmini.store.controller;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.config.util.PageAttr;
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

    @ModelAttribute
    public void categories(@PathVariable String category, @PathVariable String subCategory, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
    }

    // 카테고리별 게시물 조회
    @GetMapping
    public String viewBoardList(
//            @PathVariable("category") String categoryName,
//            @PathVariable("subCategory") String subCategoryName,
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @ModelAttribute SearchBoardDto searchBoardDto,
            Model model) {
        log.debug("category = {}, subCategory = {}", searchBoardDto.getCategory(), searchBoardDto.getSubCategory());
        log.debug("SearchBoardDto = {}", searchBoardDto);

        String order = searchBoardDto.getOrder();
        if (StringUtils.hasText(order) && CustomPageUtils.isValid(order)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, order, "createdDate"));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, "createdDate"));
        }

        Page<BoardDto> results = boardService.viewBoardList(pageable, searchBoardDto).map(BoardDto::toDto);
        for (BoardDto result : results.getContent()) {
            log.trace("result = {}", result);
        }

        PageAttr pageAttr = CustomPageUtils.getPageAttr(results, 5);
        model.addAttribute("startPage", pageAttr.getStartPage());
        model.addAttribute("endPage", pageAttr.getEndPage());
        model.addAttribute("prev", pageAttr.isPrev());
        model.addAttribute("next", pageAttr.isNext());
        model.addAttribute("searchType", searchBoardDto.getSearchType());
        model.addAttribute("searchKeyword", searchBoardDto.getSearchKeyword());
        model.addAttribute("results", results);
        return "board/boardList";
    }
}

