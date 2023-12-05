package com.kmini.store.controller.api;


import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/board/trade/{subCategory}")
@RequiredArgsConstructor
@Slf4j
public class ItemBoardApiController {

    private final ItemBoardService itemBoardService;
    private static final String category = "trade";

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deletePost(
                       @PathVariable("subCategory") String subCategory,@PathVariable("boardId") Long boardId) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);
        itemBoardService.deletePost(boardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<Void>(1, "성공", null));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<?> updatePost(@Validated ItemBoardUpdateFormDto itemBoardUpdateFormDto, BindingResult bindingResult, Model model) {
        log.debug("itemBoardUpdateFormDto = {}", itemBoardUpdateFormDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("itemBoardUpdateFormDto", itemBoardUpdateFormDto);
        }

        itemBoardService.updatePost(itemBoardUpdateFormDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<Void>(1, "성공", null));
    }
}
