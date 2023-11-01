package com.kmini.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    // 전체 카테고리 게시판 클릭
    @GetMapping("/{topCategory}")
    public String category(@PathVariable("topCategory") String topCategory, Model model) {
        model.addAttribute("topCategory", topCategory);
        System.out.println("topCategory = " + topCategory);
        return "board/" + topCategory;
    }

    // 특정 카테고리 게시글 클릭
    @GetMapping("/{topCategory}/{boardId}")
    public String board(@PathVariable("topCategory") String topCategory,
                        @PathVariable("boardId") int boardId, Model model) {

        return "board/trade-detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

}
