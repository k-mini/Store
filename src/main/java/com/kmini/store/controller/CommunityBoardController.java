package com.kmini.store.controller;

import com.kmini.store.domain.CommunityBoard;
import com.kmini.store.dto.request.BoardDto.CommunityBoardSaveReqDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardSaveReqDto;
import com.kmini.store.dto.response.CommunityBoardDto.CommunityBoardViewRespDto;
import com.kmini.store.service.CommunityBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CommunityBoardService communityBoardService;

    @ModelAttribute
    public void categories(@PathVariable String subCategory, Model model) {
        model.addAttribute("subCategory", subCategory);
    }

    // 게시물 자세히 조회
    @GetMapping("/{boardId}")
    public String viewBoard(
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId, Model model) {

        CommunityBoardViewRespDto result = communityBoardService.viewBoard(boardId);

        model.addAttribute("result", result);
        log.debug("result = {}", result);
        return "board/communitydetail";
    }

    // 게시물 작성 화면
    @GetMapping("/form")
    public String form(
            @ModelAttribute ItemBoardSaveReqDto itemBoardSaveReqDto, Model model) {
        return "board/form";
    }

    // 게시물 저장
    @PostMapping("/form")
    public String saveBoard(
            @ModelAttribute CommunityBoardSaveReqDto communityBoardSaveReqDto,
            RedirectAttributes redirectAttributes) throws IOException {
        log.debug("communityBoardFormSaveDto = {}", communityBoardSaveReqDto);

        CommunityBoard newCommunityBoard = CommunityBoard.builder()
                                                        .subCategoryName(communityBoardSaveReqDto.getSubCategoryName())
                                                        .title(communityBoardSaveReqDto.getTitle())
                                                        .content(communityBoardSaveReqDto.getContent())
                                                        .file(communityBoardSaveReqDto.getFile())
                                                        .build();
        communityBoardService.save(newCommunityBoard);

        redirectAttributes.addAttribute("category", "community");
        redirectAttributes.addAttribute("subCategory", communityBoardSaveReqDto.getSubCategoryName());
        return "redirect:/boards/{category}/{subCategory}";
    }

}
