package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.request.BoardDto.FormSaveDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.service.BoardService;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static com.kmini.store.domain.type.CategoryType.COMMUNITY;
import static com.kmini.store.domain.type.CategoryType.TRADE;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/boards/{category}/{subCategory}")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ItemBoardService itemBoardService;
//    private final CommunityBoardService communityBoardService;

    // 게시글 작성
    @GetMapping("/form")
    public String save(
            @PathVariable("category") String category,
            @PathVariable("subCategory") String subCategory, Model model) {
        // PathVariable 자동 modelAttribute 저장.
        model.addAttribute("formSaveDto", new FormSaveDto());
        return "board/form";
    }

    // 카테고리별 게시물 조회
    @GetMapping
    public String load(
            @PathVariable("category") String categoryName,
            @PathVariable("subCategory") String subCategoryName,
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            Model model) {
        log.info("category = {}, subCategory = {}", categoryName, subCategoryName);
        Page<BoardDto> results = boardService.load(pageable, categoryName, subCategoryName);
        for (BoardDto result : results.getContent()) {
            log.debug("result = {}", result);
        }

        CustomPageUtils.configure(results,5, model);
        model.addAttribute("results", results);
        return "board/boardlist";
    }

    @PostMapping("/form")
    public String save(
            @ModelAttribute FormSaveDto formSaveDto,
            @PathVariable("category") String categoryName,
            @PathVariable("subCategory") String subCategoryName,
            @AuthenticationPrincipal PrincipalDetail principal, RedirectAttributes redirectAttributes) throws IOException {
        log.info("formSaveDto = {}", formSaveDto);

        if (categoryName.equals(TRADE.getNameWithLowerCase())) {
            formSaveDto.setCategory(categoryName);
            formSaveDto.setSubCategory(subCategoryName);
            itemBoardService.save(formSaveDto, principal);
        } else if (categoryName.equals(COMMUNITY.getNameWithLowerCase())) {

        }

        redirectAttributes.addAttribute("category", categoryName);
        redirectAttributes.addAttribute("subCategory", subCategoryName);
        return "redirect:/boards/{category}/{subCategory}";
    }

//     게시물 상세 조회
    @GetMapping("/{boardId}")
    public String detail(
            @PathVariable("categoryName") String categoryName,
            @PathVariable("boardId") Long boardId, Model model) {
//        ItemBoardRespDetailDto result = boardService.detail(boardId);
//        model.addAttribute("result",result);
        return "board/trade-detail";
    }
}

