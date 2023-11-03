package com.kmini.store.controller;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.BoardRespDto;
import com.kmini.store.dto.ItemBoardRespDto;
import com.kmini.store.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/boards/{categoryName}")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @GetMapping("/form")
    public String saveForm(@PathVariable String categoryName, Model model) {
        model.addAttribute("categoryName", categoryName);
        return "board/form";
    }

    // 게시물 조회
    @GetMapping
    public String load(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            Model model) {
        Page<BoardRespDto> results = boardService.load(pageable);
        for (BoardRespDto result : results.getContent()) {
            log.debug("result = {}", result);
        }

        CustomPageUtils.configure(results,5, model);

        model.addAttribute("results", results);
        return "board/trade";
    }

    // 게시물 자세히 조회
//    @GetMapping("/{boardId}")
//    public String detail(@PathVariable("boardId") Long boardId,
//                         @RequestParam("categoryId") Long categoryId, Model model) {
//        ItemBoardRespDto.ItemBoardRespDetailDto result = boardService.detail(boardId, categoryId);
//        model.addAttribute("result",result);
//        return "board/trade-detail";
//    }
}

