package com.kmini.store.controller;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.dto.ItemBoardUploadDto;
import com.kmini.store.dto.RespDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/boards/trade")
@Slf4j
@RequiredArgsConstructor
public class ItemBoardController {

    private final ItemBoardService itemBoardService;

    // 거래 카테고리 게시판 클릭
    @GetMapping
    public String category() {
        return "board/trade";
    }

    // 특정 거래 게시글 클릭
    @GetMapping("/{boardId}")
    public String board(@PathVariable("boardId") int boardId, Model model) {
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
        log.info("ItemBoardUploadDto = {}", itemBoardUploadDto);
        itemBoardService.upload(itemBoardUploadDto, principal);
        return "board/trade";
    }
}
