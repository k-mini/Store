package com.kmini.store.controller.api;


import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.dto.response.ItemBoardDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardUpdateRespDto;
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
    public ResponseEntity<?> updatePost(@PathVariable Long boardId,
                                        @Validated ItemBoardUpdateFormDto itemBoardUpdateReqDto, BindingResult bindingResult,
                                        Model model) {
        log.debug("itemBoardUpdateReqDto = {}", itemBoardUpdateReqDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("itemBoardUpdateReqDto", itemBoardUpdateReqDto);
        }

        ItemBoard itemBoard = itemBoardService.updatePost(itemBoardUpdateReqDto, boardId);

        ItemBoardUpdateRespDto result = ItemBoardUpdateRespDto.toDto(itemBoard);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
