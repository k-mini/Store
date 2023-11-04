package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.request.ItemBoardDto.UploadDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.kmini.store.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/boards/{category}/{subCategory}")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @GetMapping("/form")
    public String save(@PathVariable String category, Model model) {
        model.addAttribute("categoryName", category);
        return "board/form";
    }

    // 카테고리별 게시물 조회
    @GetMapping
    public String load(
            @PathVariable("category") String category,
            @PathVariable("subCategory") String subCategoryName,
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            Model model) {
        log.info("category = {}, subCategory = {}", category, subCategoryName);
        Page<BoardDto> results = boardService.load(pageable, category, subCategoryName);
        for (BoardDto result : results.getContent()) {
            log.debug("result = {}", result);
        }

        CustomPageUtils.configure(results,5, model);
        model.addAttribute("results", results);
        return "board/trade";
    }

//    @PostMapping("/form")
    public String save(
            @ModelAttribute UploadDto uploadDto,
            @AuthenticationPrincipal PrincipalDetail principal) throws IOException {
        log.info("uploadDto = {}", uploadDto);
        boardService.save(uploadDto, principal);
        return "redirect:/boards/trade";
    }

    // 게시물 상세 조회
//    @GetMapping("/{boardId}")
    public String detail(
            @PathVariable("categoryName") String categoryName,
            @PathVariable("boardId") Long boardId, Model model) {
//        ItemBoardRespDetailDto result = boardService.detail(boardId);
//        model.addAttribute("result",result);
        return "board/trade-detail";
    }
}

