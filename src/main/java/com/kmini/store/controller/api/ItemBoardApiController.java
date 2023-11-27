package com.kmini.store.controller.api;


import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.service.impl.ItemBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ItemBoardServiceImpl itemBoardService;
    private static final String category = "trade";

    @DeleteMapping("/{boardId}")
    public String delete(
                       @PathVariable("subCategory") String subCategory,@PathVariable("boardId") Long boardId,
                       RedirectAttributes redirectAttributes) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);
        itemBoardService.delete(boardId);

        redirectAttributes.addAttribute("subCategory", subCategory);
        return "redirect:/boards/trade/{subCategory}";
    }

    @PatchMapping("/{boardId}")
    public String update(@Validated ItemBoardUpdateFormDto itemBoardUpdateFormDto, BindingResult bindingResult, Model model) {
        log.debug("itemBoardUpdateFormDto = {}", itemBoardUpdateFormDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("itemBoardUpdateFormDto", itemBoardUpdateFormDto);
        }

        itemBoardService.update(itemBoardUpdateFormDto);
        return "board/updateform";
    }
}
