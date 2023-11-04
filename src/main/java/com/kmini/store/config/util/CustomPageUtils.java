package com.kmini.store.config.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

@Slf4j
public class CustomPageUtils {


    // gap: 구간
    public static void configure(Page<?> page, int gap, Model model) {

        int pageNum = page.getNumber() + 1;

        // 현재 페이지 기준 마지막 페이지
        int endPage = (int) (Math.ceil((double) pageNum / gap )) * gap ;
        // 시작 페이지
        int startPage = endPage - gap + 1;

        // 페이지 총 개수
        int totalPages = page.getTotalPages();
        // 페이지가 하나도 없더라도 1로 조정
        totalPages = totalPages == 0 ? 1 : totalPages;

        // 마지막 페이지 조정
        if (totalPages < endPage) {
            endPage = totalPages;
        }

        //  이전 섹션으로 이동 가능한지
        boolean prev = startPage > 1;
        // 다음 섹션으로 이동 가능한지
        boolean next = endPage < totalPages;

        log.info("startPage={}",startPage);
        log.info("endPage={}",endPage);
        log.info("prev={}",prev);
        log.info("next={}",next);

        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("prev",prev);
        model.addAttribute("next",next);
    }
}
