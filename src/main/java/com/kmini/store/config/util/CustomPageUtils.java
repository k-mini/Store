package com.kmini.store.config.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.*;

@Slf4j
public class CustomPageUtils {

    private static final Set<String> orders = new HashSet<>(Arrays.asList("views","createdDate"));

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

        log.debug("startPage={}",startPage);
        log.debug("endPage={}",endPage);
        log.debug("prev={}",prev);
        log.debug("next={}",next);

        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("prev",prev);
        model.addAttribute("next",next);
    }

    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, Path<?> path) {
        return pageable.getSort()
                .stream()
                .map(order -> getOrderSpecifier(order, path))
                .toArray(OrderSpecifier[] :: new);
    }

    private static OrderSpecifier<?> getOrderSpecifier(Sort.Order order, Path<?> path) {
        PathBuilder<?> pathBuilder = new PathBuilder<>(path.getType(), path.getMetadata());

        return new OrderSpecifier(checkOrder(order.getDirection()), pathBuilder.get(order.getProperty()));
    }



    private static Order checkOrder(Sort.Direction direction) {
        return direction.isAscending() ? Order.ASC : Order.DESC;
    }

    public static boolean isValid(String order) {
        return orders.contains(order);
    }
}
