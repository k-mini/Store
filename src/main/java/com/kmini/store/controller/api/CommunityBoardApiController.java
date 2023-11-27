package com.kmini.store.controller.api;


import com.kmini.store.service.impl.CommunityBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/board/community/{subCategory}")
@RequiredArgsConstructor
@Slf4j
public class CommunityBoardApiController {

    private final CommunityBoardServiceImpl communityBoardService;
    private static final String category = "community";

    @DeleteMapping("/{boardId}")
    public String delete(
                       @PathVariable("subCategory") String subCategory,@PathVariable("boardId") Long boardId,
                       RedirectAttributes redirectAttributes) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);
        communityBoardService.delete(boardId);

        redirectAttributes.addAttribute("subCategory", subCategory);
        return "redirect:/boards/trade/{subCategory}";
    }
}
