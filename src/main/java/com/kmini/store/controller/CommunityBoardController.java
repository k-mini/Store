package com.kmini.store.controller;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.dto.request.BoardDto.CommunityBoardFormSaveDto;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.response.CommunityBoardDto.CommunityBoardRespDetailDto;
import com.kmini.store.service.impl.CommunityBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/board/community/{subCategory}")
@Slf4j
@RequiredArgsConstructor
public class CommunityBoardController {

    private final CommunityBoardServiceImpl communityBoardService;

    // 게시물 자세히 조회
    @GetMapping("/{boardId}")
    public String detail(
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId, Model model) {
        CommunityBoardRespDetailDto result = communityBoardService.detail(boardId);
        model.addAttribute("result", result);
        log.debug("result = {}", result);
        return "communitydetail";
    }

    @GetMapping("/form")
    public String form(
            @ModelAttribute ItemBoardFormSaveDto itemBoardFormSaveDto, Model model) {
        return "board/form";
    }

    // 게시물 저장
    @PostMapping("/form")
    public String save(
            @ModelAttribute CommunityBoardFormSaveDto communityBoardFormSaveDto,
            @AuthenticationPrincipal AccountContext accountContext,
            RedirectAttributes redirectAttributes) throws IOException {
        log.debug("communityBoardFormSaveDto = {}", communityBoardFormSaveDto);
        communityBoardService.save(communityBoardFormSaveDto);

        redirectAttributes.addAttribute("category", "community");
        redirectAttributes.addAttribute("subCategory", communityBoardFormSaveDto.getSubCategory());
        return "redirect:/boards/{category}/{subCategory}";
    }

}
