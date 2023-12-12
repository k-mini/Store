package com.kmini.store.controller;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.request.TradeDto.SelectUserTradeHistoryReqDto;
import com.kmini.store.dto.response.TradeDto.TradeHistoryRespDto;
import com.kmini.store.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/user/{userId}/trade-history")
    public String selectUserTradeHistory(@PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                                         SelectUserTradeHistoryReqDto selectUserTradeHistoryReqDto, Model model) {
        log.debug("tradeHistoryReqDto = {}", selectUserTradeHistoryReqDto);

        Page<TradeHistoryRespDto> results = tradeService.selectUserTradeHistory(selectUserTradeHistoryReqDto, pageable);

        log.debug("results = {}", results);
        CustomPageUtils.configure(results, 5, model);
        model.addAttribute("sType", selectUserTradeHistoryReqDto.getSType());
        model.addAttribute("s", selectUserTradeHistoryReqDto.getS());
        model.addAttribute("results", results);
        return "board/tradehistory";
    }


}
