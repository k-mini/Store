package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.ItemBoardRespDto.ItemBoardRespAllDto;
import com.kmini.store.dto.ItemBoardRespDto.ItemBoardRespDetailDto;
import com.kmini.store.dto.ItemBoardUploadDto;
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

import java.io.IOException;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/boards/trade")
@Slf4j
@RequiredArgsConstructor
public class ItemBoardController {

    private final ItemBoardService itemBoardService;

    // 게시물 조회
    @GetMapping
    public String load(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                           Model model) {
        Page<ItemBoardRespAllDto> results = itemBoardService.load(pageable);
        for (ItemBoardRespAllDto result : results.getContent()) {
            log.info("result = {}", result);
        }

        CustomPageUtils.configure(results,5, model);

        model.addAttribute("results", results);
        return "board/trade";
    }

    // 게시물 자세히 조회
    @GetMapping("/{boardId}")
    public String detail(@PathVariable("boardId") Long boardId, Model model) {
        ItemBoardRespDetailDto result = itemBoardService.detail(boardId);
        model.addAttribute("result",result);
        return "board/trade-detail";
    }

    // 게시글 작성
    @GetMapping("/saveForm")
    public String saveForm(Model model) {
        model.addAttribute("categoryName", "trade");
        return "board/saveForm";
    }

    // 게시물 저장
    @PostMapping("/saveForm")
    public String upload(
            @ModelAttribute ItemBoardUploadDto itemBoardUploadDto,
                                    @AuthenticationPrincipal PrincipalDetail principal) throws IOException {
        log.info("itemBoardUploadDto = {}", itemBoardUploadDto);
        itemBoardService.upload(itemBoardUploadDto, principal);
        return "redirect:/boards/trade";
    }
}
