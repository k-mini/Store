package com.kmini.store.controller;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.dto.request.TradeDto;
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
    public String viewTradeHistory(@PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
                                   TradeDto.TradeHistoryReqDto tradeHistoryReqDto, Model model) {
        log.debug("tradeHistoryReqDto = {}", tradeHistoryReqDto);
        Page<TradeHistoryRespDto> results = tradeService.viewTradeHistory(tradeHistoryReqDto, pageable);

        log.debug("results = {}", results);
//        results.getContent().get(0).getTradeStatus().equals(TradeStatus.WAIT);
        CustomPageUtils.configure(results, 5, model);
        model.addAttribute("sType", tradeHistoryReqDto.getSType());
        model.addAttribute("s", tradeHistoryReqDto.getS());
        model.addAttribute("results", results);
        return "board/tradehistory";
    }


}
