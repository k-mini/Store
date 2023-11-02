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
@RequestMapping("/boards/community")
@Slf4j
@RequiredArgsConstructor
public class CommunityBoardController {

    private final ItemBoardService itemBoardService;

    // 커뮤니티 게시판 클릭
    @GetMapping
    public String category() {
        return "board/community";
    }

    // 특정 커뮤니티 게시글 클릭
    @GetMapping("/{boardId}")
    public String board(@PathVariable("boardId") int boardId, Model model) {

        return "board/trade-detail";
    }

    // 게시글 작성
    @GetMapping("/saveForm")
    public String saveForm(Model model) {
        model.addAttribute("categoryName", "community");
        return "board/saveForm";
    }

    // 게시물 저장
    @PostMapping("/saveForm")
    public ResponseEntity<?> upload(@ModelAttribute ItemBoardUploadDto itemBoardUploadDto,
                                    @AuthenticationPrincipal PrincipalDetail principal) throws IOException {
        log.info("ItemBoardUploadDto = {}", itemBoardUploadDto);
//        itemBoardService.upload(itemBoardUploadDto);
        return ResponseEntity.ok(new RespDto<Void>(1, "성공", null));
    }

}
