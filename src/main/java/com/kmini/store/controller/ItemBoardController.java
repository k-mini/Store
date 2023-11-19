package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/board/trade/{subCategory}")
@Slf4j
@RequiredArgsConstructor
public class ItemBoardController {

    private final ItemBoardService itemBoardService;

    // 게시물 자세히 조회
    @GetMapping("/{boardId}")
    public String viewBoard(
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId, Model model) {
        CategoryType.checkType(subCategory);
        ItemBoardRespDetailDto result = itemBoardService.viewBoard(boardId);
        model.addAttribute("result", result);
        log.debug("result = {}", result);
        return "board/trade-detail";
    }
    
    // 게시물 수정
    @GetMapping("/{boardId}/form")
    public String updateBoard(@PathVariable Long boardId,
                              @PathVariable String subCategory, Model model) {

        ItemBoardUpdateFormDto result = itemBoardService.getUpdateForm(boardId);
        model.addAttribute("itemBoardUpdateFormDto", result);
        log.debug("result = {}", result);
        return "board/updateform";
    }
    
    // 게시물 생성
    @GetMapping("/form")
    public String saveBoard(
            @ModelAttribute ItemBoardFormSaveDto itemBoardFormSaveDto, Model model) {
        // PathVariable 자동 modelAttribute 저장.
        model.addAttribute("itemBoardFormSaveDto", new ItemBoardFormSaveDto());
        return "board/form";
    }

    @PostMapping("/form")
    public String saveBoard(
            @ModelAttribute ItemBoardFormSaveDto itemBoardFormSaveDto,
            @PathVariable("subCategory") String subCategoryName,
            @AuthenticationPrincipal PrincipalDetail principal, RedirectAttributes redirectAttributes) throws IOException {
        log.debug("formSaveDto = {}", itemBoardFormSaveDto);
        log.debug("subCategory = {}", subCategoryName);

        itemBoardService.save(itemBoardFormSaveDto, principal);

        redirectAttributes.addAttribute("category", "trade");
        redirectAttributes.addAttribute("subCategory", subCategoryName);
        return "redirect:/boards/{category}/{subCategory}";
    }
}
