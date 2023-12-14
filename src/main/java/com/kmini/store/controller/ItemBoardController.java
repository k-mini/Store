package com.kmini.store.controller;

import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.request.BoardDto.ItemBoardSaveReqDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateReqDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @ModelAttribute
    public void categories(@PathVariable String subCategory, Model model) {
        model.addAttribute("subCategory", subCategory);
    }

    // 게시물 자세히 조회
    @GetMapping("/{boardId}")
    public String viewBoard(
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId, Model model) {

        ItemBoardViewRespDto result = itemBoardService.viewBoard(boardId);

        model.addAttribute("result", result);
        log.debug("result = {}", result);
        return "board/tradedetail";
    }
    
    // 게시물 수정
    @GetMapping("/{boardId}/form")
    public String getUpdateForm(@PathVariable Long boardId,
                              @PathVariable String subCategory, Model model) {

        ItemBoardUpdateReqDto result = itemBoardService.getUpdateForm(boardId);

        model.addAttribute("itemBoardUpdateReqDto", result);
        return "board/updateform";
    }
    
    // 게시물 생성
    @GetMapping("/form")
    public String getSaveForm(
            @ModelAttribute ItemBoardSaveReqDto itemBoardSaveReqDto, Model model) {
        // PathVariable 자동 modelAttribute 저장.
        model.addAttribute("itemBoardFormSaveDto", new ItemBoardSaveReqDto());
        return "board/form";
    }

    // 게시물 등록
    @PostMapping("/form")
    public String saveBoard(
            @ModelAttribute ItemBoardSaveReqDto itemBoardSaveReqDto,
            @PathVariable("subCategory") String subCategoryName, RedirectAttributes redirectAttributes) throws IOException {
        log.debug("itemBoardSaveReqDto = {}", itemBoardSaveReqDto);
        log.debug("subCategory = {}", subCategoryName);

        ItemBoard itemBoard = ItemBoard.builder()
                                        .title(itemBoardSaveReqDto.getTitle())
                                        .content(itemBoardSaveReqDto.getContent())
                                        .file(itemBoardSaveReqDto.getFile())
                                        .itemName(itemBoardSaveReqDto.getItemName())
                                        .build();

        itemBoardService.saveBoard(itemBoard, subCategoryName);

        redirectAttributes.addAttribute("category", "trade");
        redirectAttributes.addAttribute("subCategory", subCategoryName);
        return "redirect:/boards/{category}/{subCategory}";
    }
}
